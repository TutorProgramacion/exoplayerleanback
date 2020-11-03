package com.carmelo.exoplayer_leanback;

import android.content.Context;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.leanback.app.PlaybackSupportFragment;
import androidx.leanback.app.PlaybackSupportFragmentGlueHost;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.widget.ArrayObjectAdapter;

public class LeanbackFragmentPlayer extends PlaybackSupportFragment
        implements Player.EventListener, VideoPlayerGlue.OnActionClickedListener {

    private SimpleExoPlayer player;
    private VideoPlayerGlue playerGlue;

    private void preparePlayer(Context context, ExoPlayer player, String videoUri) {

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "receiver"));

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource =
                new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(videoUri));

        // Prepare the player with the source.
        player.setMediaSource(videoSource);
        player.prepare();
    }

    private void initPlayer() {

        PlayerView playerView = getActivity().findViewById(R.id.player);

        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);

        PlayerAdapter playerAdapter = new LeanbackPlayerAdapter(getContext(), player, 16);

        playerGlue = new VideoPlayerGlue(getContext(), playerAdapter, this);
        playerGlue.setHost(new PlaybackSupportFragmentGlueHost(this));
        playerGlue.playWhenPrepared();

        setAdapter(new ArrayObjectAdapter());

        String url = "https://multiplatform-f.akamaihd.net/i/multi/will/bunny/big_buck_bunny_,640x360_400,640x360_700,640x360_1000,950x540_1500,.f4v.csmil/master.m3u8";
        preparePlayer(getContext(), player, url);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT < 24 || player == null)) {
            initPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    public void onPrevious() {

    }

    @Override
    public void onNext() {

    }

}
