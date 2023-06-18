package com.example.new_gpt

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private fun updateBoard() {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                // Получаем значение на кнопке
                val value = board[i][j]

                // Получаем индекс кнопки
                val buttonId = resources.getIdentifier("button_$i$j", "id", packageName)

                // Получаем соответствующую кнопку
                val button = findViewById<Button>(buttonId)

                if (value == 0) {
                    // Если значение на кнопке равно 0, то кнопка пустая
                    button.text = ""
                    button.setBackgroundResource(R.drawable.empty_button)
                } else {
                    // Иначе, отображаем значение на кнопке
                    button.text = value.toString()
                    button.setBackgroundResource(R.drawable.number_button)
                }
            }
        }
    }

    private fun generateBoard() {
        val values = mutableListOf<Int>()

        // Заполняем список значениями от 1 до BOARD_SIZE^2-1
        for (i in 1 until BOARD_SIZE * BOARD_SIZE) {
            values.add(i)
        }

        // Перемешиваем список
        values.shuffle()

        // Распределяем значения по игровому полю
        var index = 0
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                if (index < values.size) {
                    board[i][j] = values[index]
                    index++
                }
            }
        }

        // Устанавливаем индексы пустой кнопки
        emptyRow = BOARD_SIZE - 1
        emptyCol = BOARD_SIZE - 1
        board[emptyRow][emptyCol] = 0
    }

    private fun checkWin(): Boolean {
        // Проверяем, что числа на кнопках расположены в нужном порядке
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                val value = i * BOARD_SIZE + j + 1
                if (board[i][j] != value % (BOARD_SIZE * BOARD_SIZE)) {
                    return false
                }
            }
        }

        // Проверяем, что пустая кнопка находится в правом нижнем углу
        return board[BOARD_SIZE - 1][BOARD_SIZE - 1] == 0
    }


    var emptyRow = 4
    var emptyCol = 4
    private val BOARD_SIZE = 4

    private var board = Array(BOARD_SIZE) { IntArray(BOARD_SIZE) }
    private var buttons = Array(BOARD_SIZE) { arrayOfNulls<Button>(BOARD_SIZE) }
    private lateinit var newGameButton: Button
    private lateinit var textView2: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView2 = findViewById(R.id.textView2)

        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                val buttonId = resources.getIdentifier("button_$i$j", "id", packageName)
                buttons[i][j] = findViewById(buttonId)
                buttons[i][j]?.setOnClickListener {
                    // Получаем индексы кнопки
                    val row = i
                    val col = j

                    // Получаем значение на кнопке
                    val value = board[row][col]

                    // Получаем значения соседних кнопок
                    val topValue = if (row > 0) board[row - 1][col] else -1
                    val bottomValue = if (row < BOARD_SIZE - 1) board[row + 1][col] else -1
                    val leftValue = if (col > 0) board[row][col - 1] else -1
                    val rightValue = if (col < BOARD_SIZE - 1) board[row][col + 1] else -1

                    // Проверяем, можно ли переместить значение на данную кнопку
                    if (topValue == 0 || bottomValue == 0 || leftValue == 0 || rightValue == 0) {
                        // Меняем значения на кнопке и на пустой кнопке
                        board[row][col] = 0
                        board[emptyRow][emptyCol] = value

                        // Обновляем индексы пустой кнопки
                        emptyRow = row
                        emptyCol = col

                        // Обновляем игровое поле на экране
                        updateBoard()
                    }
                    if (checkWin())
                        textView2.text = "WIN!!!"

                }
            }

            newGameButton = findViewById(R.id.new_game_button)
            newGameButton.setOnClickListener {
                // Сбрасываем значения в массиве board
                for (i in 0 until BOARD_SIZE) {
                    for (j in 0 until BOARD_SIZE) {
                        board[i][j] = (i * BOARD_SIZE) + j + 1
                    }
                }
                board[BOARD_SIZE - 1][BOARD_SIZE - 1] = 0

                // Генерируем новое игровое поле
                generateBoard()

                // Обновляем игровое поле на экране
                updateBoard()
            }

            generateBoard()
            updateBoard()
        }
    }
}