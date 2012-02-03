package gr.academic.city.msc.industrial.mobileclickers;

import gr.academic.city.msc.industrial.mobileclickers.util.Communicator;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FetchQuestionActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fetch_question);
	}

	public void fetchQuestion(View v) {
		new FetchQuestionAsyncTask()
				.execute(((EditText) findViewById(R.id.questionCodeTextView))
						.getText().toString().trim());
	}

	private class FetchQuestionAsyncTask extends
			AsyncTask<String, Void, Integer> {
		
		private String questionCode;
		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				questionCode = params[0];
				return Communicator.getInstance().fetchQuestion(questionCode);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			//TODO: handle -1!!
			
			Intent answerQuestionIntent = new Intent(getApplicationContext(), AnswerQuestionActivity.class);
			answerQuestionIntent.putExtra(AnswerQuestionActivity.NUMBER_OF_ANSWERS_EXTRAS, result);
			answerQuestionIntent.putExtra(AnswerQuestionActivity.QUESTION_CODE, questionCode);
			
			getApplicationContext().startActivity(answerQuestionIntent);
		}
	}
}
