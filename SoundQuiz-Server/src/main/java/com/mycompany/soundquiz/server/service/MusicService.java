/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.service;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author Admin
 */
public class MusicService {
    
    private static final Random random = new Random();
    private static MusicService instance;
    private final DatabaseService dbService;

    private MusicService() throws Exception {
        this.dbService = DatabaseService.getInstance();
    }

    public static MusicService getInstance() throws Exception {
        if (instance == null) {
            instance = new MusicService();
        }
        return instance;
    }

    /**
     * Lấy n bài hát ngẫu nhiên từ mood list
     */
    public List<Map<String, Object>> getRandomSongs(List<Integer> moodIds, int n) throws Exception {
        if (moodIds.isEmpty()) {
            throw new IllegalArgumentException("Mood list is empty");
        }

        String inClause = String.join(",", moodIds.stream().map(String::valueOf).toArray(String[]::new));
        String sql = "SELECT * FROM music WHERE mood_id IN (" + inClause + ") ORDER BY RAND() LIMIT " + n;
        return dbService.executeQuery(sql);
    }
    
    public List<Map<String, Object>> getAllMoods() throws SQLException {
        String sql = "SELECT * FROM music_mood ORDER BY id";
        return dbService.executeQuery(sql);
    }

    /**
     * Cắt 5 giây WAV từ vị trí ngẫu nhiên
     */
    public byte[] cutRandomWavSegment(InputStream inputStream, int segmentSeconds) throws Exception {
        try (AudioInputStream originalStream = AudioSystem.getAudioInputStream(inputStream)) {
            AudioFormat format = originalStream.getFormat();
            long totalFrames = originalStream.getFrameLength();
            long segmentFrames = (long) (segmentSeconds * format.getFrameRate());

            if (segmentFrames > totalFrames) {
                segmentFrames = totalFrames;
            }

            long startFrame = (long) ((totalFrames - segmentFrames) * random.nextDouble());
            long bytesToSkip = startFrame * format.getFrameSize();

            byte[] buffer = new byte[8192];
            while (bytesToSkip > 0) {
                long skipped = originalStream.skip(bytesToSkip);
                if (skipped > 0) {
                    bytesToSkip -= skipped;
                } else {
                    int toRead = (int) Math.min(buffer.length, bytesToSkip);
                    int read = originalStream.read(buffer, 0, toRead);
                    if (read <= 0) {
                        break;
                    }
                    bytesToSkip -= read;
                }
            }

            AudioInputStream segmentStream = new AudioInputStream(originalStream, format, segmentFrames);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            AudioSystem.write(segmentStream, AudioFileFormat.Type.WAVE, out);
            return out.toByteArray();
        }
    }
}
