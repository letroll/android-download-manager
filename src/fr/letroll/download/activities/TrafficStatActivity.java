package fr.letroll.download.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.letroll.download.R;

import fr.letroll.download.utils.ConfigUtils;
import fr.letroll.download.utils.StorageUtils;

public class TrafficStatActivity extends Activity {

	private TextView netText;
	private TextView appWifiText;
	private TextView appGprsText;
	private TextView testText;
	private Button clearButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.traffic_stat_activity);

		netText = (TextView) findViewById(R.id.net_text);
		appWifiText = (TextView) findViewById(R.id.app_wifi_text);
		appGprsText = (TextView) findViewById(R.id.app_gprs_text);
		testText = (TextView) findViewById(R.id.test_text);
		clearButton = (Button) findViewById(R.id.btn_clear);
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearTrafficStats();
			}
		});
		getTrafficStats();
	}

	private void clearTrafficStats() {
		ConfigUtils.setLong(this, ConfigUtils.KEY_RX_MOBILE, 0L);
		ConfigUtils.setLong(this, ConfigUtils.KEY_RX_WIFI, 0L);
		ConfigUtils.setLong(this, ConfigUtils.KEY_TX_MOBILE, 0L);
		ConfigUtils.setLong(this, ConfigUtils.KEY_TX_WIFI, 0L);
		ConfigUtils.setString(this, ConfigUtils.KEY_Network_Operator_Name, "");
		getTrafficStats();
	}

	private void getTrafficStats() {
		ConnectivityManager conn = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conn.getActiveNetworkInfo() == null) {
			netText.setText(getString(R.string.network_not_connected) + "\n"+getString(R.string.mobile_operators)+"：" + ConfigUtils.getString(this, ConfigUtils.KEY_Network_Operator_Name));
		} else {
			netText.setText(getString(R.string.current_network_) + conn.getActiveNetworkInfo().getTypeName() + "\n"+getString(R.string.mobile_operators)+"：" + ConfigUtils.getString(this, ConfigUtils.KEY_Network_Operator_Name));
		}

		long mobileRx = ConfigUtils.getLong(this, ConfigUtils.KEY_RX_MOBILE);
		long mobileTx = ConfigUtils.getLong(this, ConfigUtils.KEY_TX_MOBILE);
		appGprsText.setText(getString(R.string.reception_) + StorageUtils.size(mobileRx) + " / "+getString(R.string.send_) + StorageUtils.size(mobileTx));

		long wifiRx = ConfigUtils.getLong(this, ConfigUtils.KEY_RX_WIFI);
		long wifiTx = ConfigUtils.getLong(this, ConfigUtils.KEY_TX_WIFI);
		appWifiText.setText(getString(R.string.reception_) + StorageUtils.size(wifiRx) + " / "+getString(R.string.send_) + StorageUtils.size(wifiTx));

		try {
			PackageManager packageManager = this.getPackageManager();
			ApplicationInfo info = packageManager.getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
			testText.setText("Total: " + TrafficStats.getUidRxBytes(info.uid) + " / " + "Total: " + TrafficStats.getUidTxBytes(info.uid));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
