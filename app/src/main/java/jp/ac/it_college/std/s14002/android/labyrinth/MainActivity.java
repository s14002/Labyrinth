package jp.ac.it_college.std.s14002.android.labyrinth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LabyrinthView.Callback {

    private LabyrinthView labyrinthView;

    private int seed = 0;

    boolean isFinished = false;

    private static final String EXTRA_KEY_SEED = "key_seed";

    private static Intent newIntent(Context context, int seed) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_KEY_SEED, seed);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        seed = getIntent().getIntExtra(EXTRA_KEY_SEED, 0);

        labyrinthView = new LabyrinthView(this);
        labyrinthView.setSeed(seed);
        labyrinthView.setCallback(this);
        setContentView(labyrinthView);
    }

    @Override
    public void onGoal() {
        if (isFinished) {
            return;
        }
        isFinished = true;

        Toast.makeText(this, "Goal!!", Toast.LENGTH_SHORT).show();

        labyrinthView.stopSensor();
        labyrinthView.stopDrawThread();

        nextStage();

        finish();

    }

    private void nextStage() {
        Intent intent = MainActivity.newIntent(this, seed + 1);
        startActivity(intent);
    }

    @Override
    public void onHole() {
        if (isFinished) {
            return;
        }
        isFinished = true;

        Toast.makeText(this, "Hole!!", Toast.LENGTH_SHORT).show();

        labyrinthView.stopSensor();

        retryStage();

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        labyrinthView.startSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();

        labyrinthView.stopSensor();
    }

    private void retryStage() {
        Intent intent = MainActivity.newIntent(this, seed);
        startActivity(intent);
    }
}
