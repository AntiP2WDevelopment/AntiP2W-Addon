package addon.antip2w.modules.funny;

import addon.antip2w.modules.Categories;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

//TODO: Fix volume
public class Radio extends Module {
    private final SettingGroup sgGeneral = settings.createGroup("General");

    private final Setting<Radios> radioChannel = sgGeneral.add(new EnumSetting.Builder<Radios>()
            .name("Radio Channel")
            .defaultValue(Radios.Radio1)
            .build()
    );

    private final Setting<Integer> volumeInt = sgGeneral.add(new IntSetting.Builder()
            .name("Volume")
            .description("Adjusts the volume of the radio.")
            .defaultValue(50)
            .range(0, 100)
            .sliderRange(0, 100)
            .build()
    );

    private AdvancedPlayer player;
    private SourceDataLine audioLine;

    public Radio() {
        super(Categories.WIP, "Radio", "Its a radio bitch");
    }

    @Override
    public void onActivate() {
        playRadio();
    }

    @Override
    public void onDeactivate() {
        stopRadio();
    }

    private void playRadio() {
        try {
            String selectedRadioUrl = radioChannel.get().URL;
            if (selectedRadioUrl != null) {
                URL radioStream = new URL(selectedRadioUrl);
                player = new AdvancedPlayer(radioStream.openStream(), FactoryRegistry.systemRegistry().createAudioDevice());
                player.setPlayBackListener(new PlaybackListener() {
                    @Override
                    public void playbackFinished(PlaybackEvent evt) {
                        if (evt.getFrame() == Integer.MAX_VALUE) {
                            stopRadio();
                        }
                    }
                });

                setVolume(volumeInt.get());

                CompletableFuture.runAsync(() -> {
                    try {
                        player.play(Integer.MAX_VALUE);
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                });

                info("Playing " + radioChannel.get().toString() + "...");
            } else {
                error("Selected radio channel URL not found.");
            }
        } catch (IOException | JavaLayerException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void setVolume(int volume) throws LineUnavailableException {
        if (audioLine != null) {
            audioLine.close();
        }

        audioLine = AudioSystem.getSourceDataLine(new AudioFormat(44100, 16, 2, true, true));
        audioLine.open();
        FloatControl volumeControl = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
        float gain = volumeControl.getMaximum() * (volume / 100.0f);
        volumeControl.setValue(gain);

        audioLine.start();
    }

    private void stopRadio() {
        if (player != null) {
            player.close();
            if (audioLine != null) {
                audioLine.close();
            }
            info("Radio stopped.");
        }
    }

    public enum Radios {
        Radio1("https://icast.connectmedia.hu/5202/live.mp3"),
        SlagerFM("https://slagerfm.netregator.hu:7813/slagerfm128.mp3"),
        RetroRadio("https://icast.connectmedia.hu/5002/live.mp3"),
        BestFM("https://icast.connectmedia.hu/5102/live.mp3"),
        RockFM("https://icast.connectmedia.hu/5301/live.mp3"),
        KossuthRadio("https://icast.connectmedia.hu/4736/mr1.mp3");

        public final String URL;

        Radios(String URL) {
            this.URL = URL;
        }
    }
}
