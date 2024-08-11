package com.phinion.easyrewards

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.databinding.ActivityQuizBinding
import com.phinion.easyrewards.databinding.LoadingDialogLayoutBinding
import com.phinion.easyrewards.models.Question
import com.phinion.easyrewards.utils.CheckForInternet
import java.util.*

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var questions: ArrayList<Question>
    var index = 0
    private lateinit var question: Question
    private lateinit var timer: CountDownTimer
    var correctAnswers = 0
    private lateinit var checkForInternet: CheckForInternet
    private lateinit var loadingDialogBinding: LoadingDialogLayoutBinding
    private lateinit var loadingDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Loading Dialog
        loadingDialogBinding =
            LoadingDialogLayoutBinding.inflate(LayoutInflater.from(this))
        loadingDialog = AlertDialog.Builder(this)
            .setView(loadingDialogBinding.root)
            .setCancelable(false)
            .create()
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))
        loadingDialog.show()


        binding.quitBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        checkForInternet = CheckForInternet(this)
        mAuth = Firebase.auth
        database = Firebase.firestore
        question = Question()
        questions = ArrayList<Question>()

        val random = Random()
        val rand = random.nextInt(4)


        database.collection("quizCategories")
            .whereGreaterThanOrEqualTo("index", rand)
            .orderBy("index")
            .limit(4).get().addOnSuccessListener { queryDocumentSnapshots ->
                if (queryDocumentSnapshots.documents.size < 4) {
                    database.collection("quizCategories")
                        .whereLessThanOrEqualTo("index", rand)
                        .orderBy("index")
                        .limit(4).get().addOnSuccessListener { queryDocumentSnapshots ->
                            for (snapshot in queryDocumentSnapshots) {
                                val question = snapshot.toObject(Question::class.java)
                                questions.add(question)
                            }
                            setNextQuestion()
                        }
                } else {
                    for (snapshot in queryDocumentSnapshots) {
                        val question = snapshot.toObject(Question::class.java)
                        questions.add(question)
                    }
                    setNextQuestion()
                }
            }


        resetTimer()

    }

    fun resetTimer() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.setText((millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                Toast.makeText(baseContext, "Time Up :(", Toast.LENGTH_SHORT).show()

                val intent = Intent(baseContext, ResultActivity::class.java)
                intent.putExtra("correct", correctAnswers)
                intent.putExtra("total", questions.size)
                startActivity(intent)
                finish()
            }
        }
    }

    fun showAnswer() {
        if (question.answer
                .equals(binding.option1.text.toString())
        ) binding.option1.background =
            resources.getDrawable(R.drawable.right_answer_background) else if (question.answer
                .equals(binding.option2.text.toString())
        ) binding.option2.background =
            resources.getDrawable(R.drawable.right_answer_background) else if (question.answer
                .equals(binding.option3.text.toString())
        ) binding.option3.background =
            resources.getDrawable(R.drawable.right_answer_background) else if (question.answer
                .equals(binding.option4.text.toString())
        ) binding.option4.background =
            resources.getDrawable(R.drawable.right_answer_background)

        binding.option1.isEnabled = false
        binding.option2.isEnabled = false
        binding.option3.isEnabled = false
        binding.option4.isEnabled = false
    }

    fun setNextQuestion() {
        if (timer != null) timer.cancel()
        timer.start()
        loadingDialog.dismiss()
        if (index < questions.size) {
            binding.questionCounter.text = String.format("%d/%d", index + 1, questions.size)
            question = questions[index]
            binding.questionText.text = question.question
            binding.option1.text = question.option1
            binding.option2.text = question.option2
            binding.option3.text = question.option3
            binding.option4.text = question.option4
            binding.option1.isEnabled = true
            binding.option2.isEnabled = true
            binding.option3.isEnabled = true
            binding.option4.isEnabled = true
        }
    }

    fun checkAnswer(textView: TextView) {
        val selectedAnswer = textView.text.toString()
        if (selectedAnswer == question.answer) {
            correctAnswers++
            binding.option1.isEnabled = false
            binding.option2.isEnabled = false
            binding.option3.isEnabled = false
            binding.option4.isEnabled = false
            textView.background = resources.getDrawable(R.drawable.right_answer_background)
        } else {
            showAnswer()
            binding.option1.isEnabled = false
            binding.option2.isEnabled = false
            binding.option3.isEnabled = false
            binding.option4.isEnabled = false
            textView.background = resources.getDrawable(R.drawable.wrong_answer_background)
        }
    }

    fun reset() {
        binding.option1.background = resources.getDrawable(R.drawable.question_option_background)
        binding.option2.background = resources.getDrawable(R.drawable.question_option_background)
        binding.option3.background = resources.getDrawable(R.drawable.question_option_background)
        binding.option4.background = resources.getDrawable(R.drawable.question_option_background)
    }


    fun onClick(view: View) {

        when (view.id) {
            R.id.option1, R.id.option2, R.id.option3, R.id.option4 -> {
                if (timer != null) timer.cancel()
                val selected = view as TextView
                checkAnswer(selected)
            }
            R.id.next_btn -> {
                reset()
                if (index < questions.size-1) {
                    index++
                    setNextQuestion()
                } else {
                    val intent = Intent(
                        baseContext,
                        ResultActivity::class.java
                    )
                    intent.putExtra("correct", correctAnswers)
                    intent.putExtra("total", questions.size)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
    }
