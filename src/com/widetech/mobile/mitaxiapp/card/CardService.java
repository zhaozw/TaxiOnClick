package com.widetech.mobile.mitaxiapp.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;
import com.widetech.mobile.mitaxiapp.activity.R;

public class CardService extends Card {

	String plate = "";
	int status = 0;

	public CardService(String title, String plate, int status) {

		super(title);
		this.plate = plate;
		this.status = status;
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_service,
				null);

		((TextView) view.findViewById(R.id.title)).setText(title);

		StringBuilder plate = new StringBuilder();
		plate.append(context.getString(R.string.desc_plate_taxi));
		plate.append(" ");
		plate.append(this.plate);
		((TextView) view.findViewById(R.id.PlateOfTaxi)).setText(plate
				.toString());

		StringBuilder status = new StringBuilder();
		status.append(context.getString(R.string.status_of_service));
		status.append(" ");

		switch (this.status) {
		case 0:
			status.append("En camino");
			break;
		case 1:
			status.append("Finalizado");
			break;
		case 2:
			status.append("Cancelado");
			break;
		default:
			break;
		}

		((TextView) view.findViewById(R.id.statusService)).setText(status
				.toString());

		return view;
	}
}
