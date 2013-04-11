package com.widetech.mobile.mitaxiapp.activity;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.widetech.mobile.mitaxiapp.card.CardService;
import com.widetech.mobile.mitaxiapp.facade.FacadeMobile;
import com.widetech.mobile.mitaxiapp.object.Mobile;

import android.os.Bundle;

public class HistoryActivity extends SherlockActivity {

	private CardUI mCardView;
	private CardStack stack;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		this.getSupportActionBar().setIcon(R.drawable.logo_top);
		this.getSupportActionBar().setDisplayShowTitleEnabled(false);
		this.getSupportActionBar().setHomeButtonEnabled(true);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// init CardView
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(true);

		// create a stack
		stack = new CardStack();
		stack.setTitle(getString(R.string.label_ic_history_bar));

		addServicesToStack();
	}

	private void addServicesToStack() {
		// TODO Auto-generated method stub

		try {
			ArrayList<Mobile> data = FacadeMobile.readMobileByLastService();

			if (data != null) {

				for (int i = 0; i < data.size(); i++) {
					stack.add(new CardService(
							getString(R.string.title_row_service) + " "
									+ (i + 1), data.get(i).getPlate(), data
									.get(i).getStatus()));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// add stack to cardView
		mCardView.addStack(stack);
		// draw cards
		mCardView.refresh();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int op = item.getItemId();
		switch (op) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
