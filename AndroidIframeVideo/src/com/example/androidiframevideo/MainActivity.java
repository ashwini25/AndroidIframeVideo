package com.example.androidiframevideo;

import java.net.MalformedURLException;
import java.net.URL;
import com.squareup.picasso.Picasso;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	WebView video_webview;
	ProgressBar Pbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		video_webview = (WebView) findViewById(R.id.webView1);
		video_webview.getSettings().setJavaScriptEnabled(true);

		ImageButton btn = (ImageButton) findViewById(R.id.imageButton1);
		final TextView text = (TextView) findViewById(R.id.text);

		Pbar = (ProgressBar) findViewById(R.id.pB4);

		/** video paly code */
		WebSettings webSettings = video_webview.getSettings();
		// webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		video_webview.setBackgroundColor(0x919191);
		webSettings.setMediaPlaybackRequiresUserGesture(false);
		video_webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {

				(MainActivity.this).setProgress(progress * 100);
				{
					if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {

						Pbar.setVisibility(ProgressBar.VISIBLE);
						text.setVisibility(View.VISIBLE);

					}
					Pbar.setProgress(progress);
					text.setText("" + progress + "/100");
					if (progress == 100) {
						text.setVisibility(View.GONE);
						Pbar.setVisibility(ProgressBar.GONE);

					}
				}
			}
		});

		video_webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			}
		});

		video_webview.setDownloadListener(new DownloadListener() {

			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
			}
		});

		try {
			url = youtube_url();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Picasso picasso = Picasso.with(MainActivity.this);
		try {
			picasso.load("http://img.youtube.com/vi/" + extractYoutubeId("https://www.youtube.com/watch?v=DMz_PohEvD0")
					+ "/default.jpg").error(android.R.drawable.ic_menu_gallery)
					.placeholder(android.R.drawable.ic_menu_gallery).into(btn);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setVisibility(View.GONE);
				video_webview.loadDataWithBaseURL(null, url, "text/html", "UTF-8", "");
			}
		});
	}

	String url;

	protected String youtube_url() throws MalformedURLException {

		String sHtmlTemplate = "<iframe src='https://www.youtube.com/embed/"
				+ extractYoutubeId("https://www.youtube.com/watch?v=DMz_PohEvD0") + "?rel=0&autoplay=1'"
				+ "' width='100%' height='100%' frameborder='0' />";

		return sHtmlTemplate;
	}

	// http://img.youtube.com/vi/DMz_PohEvD0/default.jpg
	// http://img.youtube.com/vi/t7UxjpUaL3Y/0.jpg

	public String extractYoutubeId(String url) throws MalformedURLException {
		String id = null;
		try {
			String query = new URL(url).getQuery();
			String[] param = query.split("&");

			for (String row : param) {
				String[] param1 = row.split("=");
				if (param1[0].equals("v")) {
					id = param1[1];
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return id;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.video_webview.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.video_webview.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		video_webview.destroy();
	}
}
