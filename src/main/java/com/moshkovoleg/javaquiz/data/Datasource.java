package com.moshkovoleg.javaquiz.data;


import com.moshkovoleg.javaquiz.model.Answer;
import com.moshkovoleg.javaquiz.model.Question;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class Datasource {


    private static final String DB_NAME = "src/main/java/com/moshkovoleg/javaquiz/data/quiz.db";

    //    private static final String CONNECTION_STRING = "jdbc:sqlite:D:\\databases\\" + DB_NAME;
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    // parameters for answers.table
    private static final String TABLE_ANSWERS = "answers";
    private static final String COLUMN_ANSWER_ID = "id";
    private static final String COLUMN_ANSWER_QUESTION_ID = "questionId";
    private static final String COLUMN_ANSWER_TEXT = "answer";
    private static final int INDEX_ANSWER_ID = 1;
    private static final int INDEX_ANSWER_QUESTION_ID = 2;
    private static final int INDEX_ANSWER_TEXT = 3;

    // parameters for questions.table
    private static final String TABLE_QUESTIONS = "questions";
    private static final String COLUMN_QUESTION_ID = "id";
    private static final String COLUMN_QUESTION_TEXT = "text";
    private static final String COLUMN_QUESTION_CORRECT_ANSWER = "correctAnswer";
    private static final String COLUMN_QUESTION_DESCRIPTION = "description";
    private static final int INDEX_QUESTION_ID = 1;
    private static final int INDEX_QUESTION_TEXT = 2;
    private static final int INDEX_QUESTION_CORRECT_ANSWER = 3;
    private static final int INDEX_QUESTION_DESCRIPTION = 4;

    // parameters for ordering
    public static final int ORDER_BY_NONE = 1;
//    public static final int ORDER_BY_ASC = 2;
//    public static final int ORDER_BY_DESC = 3;

    private static final String QUERY_ANSWERS_BY_QUESTION_ID_START =
            "SELECT " + TABLE_ANSWERS + '.' + COLUMN_ANSWER_TEXT + " FROM " + TABLE_ANSWERS +
                    " INNER JOIN " + TABLE_QUESTIONS + " ON " + TABLE_ANSWERS + "." + COLUMN_ANSWER_QUESTION_ID +
                    " = " + TABLE_QUESTIONS + "." + COLUMN_ANSWER_QUESTION_ID +
                    " WHERE " + TABLE_QUESTIONS + "." + COLUMN_QUESTION_ID + " = \"";

//    private static final String QUERY_ANSWERS_BY_QUESTION_SORT =
//            " ORDER BY " + TABLE_ANSWERS + "." + COLUMN_ANSWER_TEXT + " COLLATE NOCASE ";

    private static final String INSERT_QUESTION = "INSERT INTO " + TABLE_QUESTIONS +
            '(' + COLUMN_QUESTION_TEXT + ", " + COLUMN_QUESTION_CORRECT_ANSWER + ", " + COLUMN_QUESTION_DESCRIPTION +") VALUES(?,?,?)";
    private static final String INSERT_ANSWER = "INSERT INTO " + TABLE_ANSWERS +
            '(' + COLUMN_ANSWER_QUESTION_ID + ", " + COLUMN_ANSWER_TEXT + ") VALUES(?, ?)";

    private static final String QUERY_QUESTION = "SELECT " + COLUMN_QUESTION_TEXT + " FROM " +
            TABLE_QUESTIONS + " WHERE " + COLUMN_QUESTION_ID + " = ?";

    private static final String QUERY_ANSWER = "SELECT " + COLUMN_ANSWER_TEXT + " FROM " +
            TABLE_ANSWERS + " WHERE " + COLUMN_ANSWER_ID + " = ?";

    private static final String QUERY_ANSWERS_BY_QUESTION_ID = "SELECT * FROM " + TABLE_ANSWERS +
            " WHERE " + COLUMN_ANSWER_QUESTION_ID + " = ?";

    private static final String UPDATE_QUESTION_TEXT = "UPDATE " + TABLE_QUESTIONS + " SET " +
            COLUMN_QUESTION_TEXT + " = ? WHERE " + COLUMN_QUESTION_ID + " = ?";

    private Connection conn;


    private PreparedStatement insertIntoQuestions;
    private PreparedStatement insertIntoAnswers;
    private PreparedStatement queryQuestion;
    private PreparedStatement queryAnswers;
    private PreparedStatement queryAnswersByQuestionId;
    private PreparedStatement updateQuestionText;

    private static Datasource instance = new Datasource();

    private Datasource() {

    }

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            System.out.println("Database connected");

            insertIntoQuestions = conn.prepareStatement(INSERT_QUESTION, Statement.RETURN_GENERATED_KEYS);
            insertIntoAnswers = conn.prepareStatement(INSERT_ANSWER, Statement.RETURN_GENERATED_KEYS);
            queryQuestion = conn.prepareStatement(QUERY_QUESTION);
            queryAnswers = conn.prepareStatement(QUERY_ANSWER);
            queryAnswersByQuestionId = conn.prepareStatement(QUERY_ANSWERS_BY_QUESTION_ID);
            updateQuestionText = conn.prepareStatement(UPDATE_QUESTION_TEXT);


            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {

            if (insertIntoQuestions != null) {
                insertIntoQuestions.close();
            }

            if (insertIntoAnswers != null) {
                insertIntoAnswers.close();
            }

            if (queryQuestion != null) {
                queryQuestion.close();
            }

            if (queryAnswers != null) {
                queryAnswers.close();
            }

            if (queryAnswersByQuestionId != null) {
                queryAnswersByQuestionId.close();
            }

            if (updateQuestionText != null) {
                updateQuestionText.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public Set<Question> queryQuestions(int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_QUESTIONS);
//        if (sortOrder != ORDER_BY_NONE) {
//            sb.append(" ORDER BY ");
//            sb.append(COLUMN_QUESTION_TEXT);
//            sb.append(" COLLATE NOCASE ");
//            if (sortOrder == ORDER_BY_DESC) {
//                sb.append("DESC");
//            } else {
//                sb.append("ASC");
//            }
//        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            Set<Question> questions = new HashSet<>();
            while (results.next()) {

                Question question = new Question();
                question.setId(results.getLong(INDEX_QUESTION_ID));
                question.setText(results.getString(INDEX_QUESTION_TEXT));
                question.setCorrectAnswer(results.getString(INDEX_QUESTION_CORRECT_ANSWER));
                question.setDescription(results.getString(INDEX_QUESTION_DESCRIPTION));
//                question.setAnswers(queryAnswersForQuestionId(question.getId().intValue()));
                questions.add(question);
            }

            return questions;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public Set<Answer> queryAnswers() {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ANSWERS);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            Set<Answer> answers = new HashSet<>();
            while (results.next()) {
                Answer answer = new Answer();
                answer.setId(results.getLong(INDEX_ANSWER_ID));
//                answer.setQuestionId((long)INDEX_ANSWER_QUESTION_ID);
                answer.setAnswer(results.getString(INDEX_ANSWER_TEXT));

                answers.add(answer);
            }

            return answers;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }
    public Set<Answer> queryAnswersForQuestionId(int id) {
        try {
            queryAnswersByQuestionId.setInt(1, id);
            ResultSet results = queryAnswersByQuestionId.executeQuery();

            Set<Answer> answers = new HashSet<>();
            while (results.next()) {
                Answer answer = new Answer();
                answer.setId(results.getLong(INDEX_ANSWER_ID));
//                answer.setQuestionId((long)id);
                answer.setAnswer(results.getString(INDEX_ANSWER_TEXT));

                answers.add(answer);
            }

            return answers;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }




    public int getCount(String table) {
        String sql = "SELECT COUNT(*) AS count FROM " + table;
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            int count = results.getInt("count");

            System.out.format("Count = %d\n", count);
            return count;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }


    private int insertQuestion(String newText,String newCorrectAnswer,String newDescription) throws SQLException {

        queryQuestion.setString(INDEX_QUESTION_TEXT, newText);

        ResultSet results = queryQuestion.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            // Insert the question
            insertIntoQuestions.setString(INDEX_QUESTION_TEXT, newText);
            insertIntoQuestions.setString(INDEX_QUESTION_CORRECT_ANSWER, newCorrectAnswer);
            insertIntoQuestions.setString(INDEX_QUESTION_DESCRIPTION, newDescription);
            int affectedRows = insertIntoQuestions.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert question!");
            }

            ResultSet generatedKeys = insertIntoQuestions.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get id for question");
            }
        }
    }

    private int insertAnswer(String text, int answerQuestion) throws SQLException {

        queryAnswers.setString(INDEX_ANSWER_TEXT, text);
        ResultSet results = queryAnswers.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            // Insert the answer
            insertIntoAnswers.setInt(INDEX_ANSWER_QUESTION_ID, answerQuestion);
            insertIntoAnswers.setString(INDEX_ANSWER_TEXT, text);
            int affectedRows = insertIntoAnswers.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert answer!");
            }

            ResultSet generatedKeys = insertIntoAnswers.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get id for answer");
            }
        }
    }

    public boolean updateQuestion(int id, String newText,String newCorrectAnswer,String newDescription) {
        try {
            updateQuestionText.setInt(INDEX_QUESTION_ID, id);
            updateQuestionText.setString(INDEX_QUESTION_TEXT, newText);
            updateQuestionText.setString(INDEX_QUESTION_CORRECT_ANSWER, newCorrectAnswer);
            updateQuestionText.setString(INDEX_QUESTION_DESCRIPTION, newDescription);

            int affectedRecords = updateQuestionText.executeUpdate();

            return affectedRecords == 1;

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

}















