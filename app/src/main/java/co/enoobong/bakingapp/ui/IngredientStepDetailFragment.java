/*
 * Copyright (c) 2017. Ibanga Enoobong Ime (World class developer and entrepreneur in transit)
 */

package co.enoobong.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import co.enoobong.bakingapp.R;
import co.enoobong.bakingapp.data.Ingredient;
import co.enoobong.bakingapp.data.Step;

import static co.enoobong.bakingapp.ui.IngredientStepActivity.PANES;
import static co.enoobong.bakingapp.ui.IngredientStepActivity.POSITION;
import static co.enoobong.bakingapp.ui.IngredientStepAdapter.INGREDIENTS;
import static co.enoobong.bakingapp.ui.IngredientStepAdapter.STEPS;


public class IngredientStepDetailFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener {

    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private final String TAG = IngredientStepDetailFragment.class.getSimpleName();
    private boolean autoPlay = false;
    private int currentWindow;
    private long playbackPosition;
    private TrackSelector trackSelector;
    private RecyclerView mIngredientsRecyclerView;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView mDescription;
    private Button mPrevious;
    private Button mNext;
    private View mStepDetail;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;
    private int mIndex;
    private int mPosition;
    private boolean mTwoPane;


    public IngredientStepDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENTS);
            mSteps = savedInstanceState.getParcelableArrayList(STEPS);
            mTwoPane = savedInstanceState.getBoolean(PANES);
            mPosition = savedInstanceState.getInt(POSITION);
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
        }

        View rootView = inflater.inflate(R.layout.fragment_ingredient_step_detail, container, false);

        mIngredientsRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_ingredients);
        mStepDetail = rootView.findViewById(R.id.step_detail_view);
        mDescription = (TextView) rootView.findViewById(R.id.tv_description);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.sepv_step_video);
        mPrevious = (Button) rootView.findViewById(R.id.bt_previous);
        mNext = (Button) rootView.findViewById(R.id.bt_next);

        mPrevious.setOnClickListener(this);
        mNext.setOnClickListener(this);

        mPosition = getArguments().getInt(POSITION);
        mIndex = mPosition - 1;
        mTwoPane = getArguments().getBoolean(PANES);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mTwoPane && mPosition == 0) {
            showIngredients();
        } else if (mTwoPane) {
            showStepsForTab();
        } else if (mPosition == 0) {
            showIngredients();
        } else {
            showStepsForPhone();
        }
    }

    private void showIngredients() {
        mStepDetail.setVisibility(View.GONE);
        mIngredientsRecyclerView.setVisibility(View.VISIBLE);
        mIngredients = getArguments().getParcelableArrayList(INGREDIENTS);
        IngredientListAdapter adapter = new IngredientListAdapter(mIngredients);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mIngredientsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        mIngredientsRecyclerView.addItemDecoration(dividerItemDecoration);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientsRecyclerView.setAdapter(adapter);
    }


    public void showStepsForTab() {
        //initializeMediaSession();
        mIngredientsRecyclerView.setVisibility(View.GONE);
        mStepDetail.setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.VISIBLE);
        mDescription.setVisibility(View.VISIBLE);
        mSteps = getArguments().getParcelableArrayList(STEPS);
        assert mSteps != null;
        mDescription.setText(mSteps.get(mIndex).getDescription());
        playStepVideo(mIndex);

    }


    private void playStepVideo(int index) {
        mPlayerView.setVisibility(View.VISIBLE);
        mPlayerView.requestFocus();
        String videoUrl = mSteps.get(index).getVideoUrl();
        String thumbNailUrl = mSteps.get(index).getThumbnailUrl();
        if (!videoUrl.isEmpty()) {
            initializePlayer(Uri.parse(videoUrl));
        } else if (!thumbNailUrl.isEmpty()) {
            initializePlayer(Uri.parse(thumbNailUrl));
        } else {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    private void showStepsForPhone() {
        showStepsForTab();
        mPrevious.setVisibility(View.VISIBLE);
        mNext.setVisibility(View.VISIBLE);
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        mExoPlayer = null;
        // Create an instance of the ExoPlayer.

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(),
                null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF);

        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

        // Set the ExoPlayer.EventListener to this activity.
        mExoPlayer.addListener(this);

        mPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(true);

        mExoPlayer.seekTo(currentWindow, playbackPosition);

        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(getActivity(), "Baking App");

        MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                new DefaultDataSourceFactory(getActivity(), BANDWIDTH_METER,
                        new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER)),
                new DefaultExtractorsFactory(), null, null);

        mExoPlayer.prepare(mediaSource);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                if (mIndex < mSteps.size() - 1) {
                    int index = ++mIndex;
                    mDescription.setText(mSteps.get(index).getDescription());
                    playStepVideo(index);
                } else {
                    Toast.makeText(getActivity(), R.string.end_of_steps, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_previous:
                if (mIndex > 0) {
                    int index = --mIndex;
                    mDescription.setText(mSteps.get(index).getDescription());
                    playStepVideo(index);
                } else {
                    Toast.makeText(getActivity(), R.string.start_of_steps, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mExoPlayer == null) {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
        outState.putParcelableArrayList(INGREDIENTS, mIngredients);
        outState.putParcelableArrayList(STEPS, mSteps);
        outState.putBoolean(PANES, mTwoPane);
        outState.putInt(POSITION, mPosition);

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            Toast.makeText(getActivity(), "Playing", Toast.LENGTH_LONG).show();
        } else if ((playbackState == ExoPlayer.STATE_READY)) {

        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            Toast.makeText(getActivity(), errorString, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }


}
