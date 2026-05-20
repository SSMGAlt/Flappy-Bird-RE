package com.dotgears.flappy;

import java.io.InputStream;

import com.dotgears.AtlasRegion;
import com.dotgears.Button;
import com.dotgears.FloatingScore;
import com.dotgears.MathTables;
import com.dotgears.Renderer;
import com.dotgears.ScorePanel;
import com.dotgears.Tweener;

public class GameScene extends Renderer {

    public static int VIRTUAL_WIDTH  = 288;
    public static int VIRTUAL_HEIGHT = 512;

    private static final int STATE_IDLE    = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_DYING   = 2;
    private static final int STATE_DEAD    = 3;
    private static final int STATE_SCORE   = 4;

    private static final int WORLD_SPEED    = 2;
    private static final int PIPE_GAP       = 100;
    private static final int PIPE_WIDTH     = 52;
    private static final int PIPE_HEIGHT    = 320;
    private static final int MAX_PIPES      = 4;

    private int groundY;
    private int skyTop;
    private int pipeSpawnInterval;
    private int panelCenterX;

    private int state;
    private int score;
    private int bestScore;
    private int gameTick;

    private Bird bird;
    private FontNumberRenderer scoreRenderer;

    private int[] pipeX;
    private int[] pipeGapY;
    private boolean[] pipePassed;
    private int pipeCount;
    private int pipeSpawnTimer;
    private int groundOffset;

    private AtlasRegion regionBgDay;
    private AtlasRegion regionBgNight;
    private AtlasRegion regionGround;
    private AtlasRegion regionPipeDown;
    private AtlasRegion regionPipeUp;
    private AtlasRegion regionPipe2Down;
    private AtlasRegion regionPipe2Up;
    private AtlasRegion regionFlash;

    private boolean useNightBg;
    private boolean useAltPipe;

    private GetReadyPanel  getReadyPanel;
    private GameOverPanel  gameOverPanel;
    private ScorePanel     scorePanel;

    private Tweener flashTweener;
    private float flashAlpha;

    private Button playButton;
    private Button leaderboardButton;

    private static final int BRONZE_THRESHOLD   = 10;
    private static final int SILVER_THRESHOLD   = 20;
    private static final int GOLD_THRESHOLD     = 30;
    private static final int PLATINUM_THRESHOLD = 40;

    public GameScene(int savedBestScore, int currentScore, InputStream atlasStream) {
        super(currentScore, savedBestScore, atlasStream);
        bestScore = savedBestScore;
        score = 0;
    }

    @Override
    public void init() {
        super.init();

        groundY        = (int) (VIRTUAL_HEIGHT * 0.781f);
        skyTop         = 0;
        pipeSpawnInterval = 90;
        panelCenterX   = VIRTUAL_WIDTH / 2;

        regionBgDay   = getSprite("bg_day");
        regionBgNight = getSprite("bg_night");
        regionGround  = getSprite("land");
        regionPipeDown  = getSprite("pipe_down");
        regionPipeUp    = getSprite("pipe_up");
        regionPipe2Down = getSprite("pipe2_down");
        regionPipe2Up   = getSprite("pipe2_up");
        regionFlash   = getSprite("white");

        useNightBg  = false;
        useAltPipe  = false;

        pipeX      = new int[MAX_PIPES];
        pipeGapY   = new int[MAX_PIPES];
        pipePassed = new boolean[MAX_PIPES];
        pipeCount  = 0;

        int birdType  = MathTables.randomRange(0, 3);
        bird          = new Bird(birdType);
        scoreRenderer = new FontNumberRenderer();
        getReadyPanel = new GetReadyPanel();
        gameOverPanel = new GameOverPanel();
        scorePanel    = C;
        flashTweener  = new Tweener();
        flashAlpha    = 0.0f;

        playButton        = new Button();
        leaderboardButton = new Button();
        playButton.setSprite("button_play");
        leaderboardButton.setSprite("button_score");

        resetWorld();
        enterIdle();
    }

    private void resetWorld() {
        score          = 0;
        pipeCount      = 0;
        pipeSpawnTimer = 0;
        groundOffset   = 0;
        gameTick       = 0;
        flashAlpha     = 0.0f;

        useNightBg = MathTables.randomRange(0, 2) == 0;
        useAltPipe = MathTables.randomRange(0, 2) == 0;

        int birdType = MathTables.randomRange(0, 3);
        bird = new Bird(birdType);
        bird.spawn(VIRTUAL_WIDTH / 5, VIRTUAL_HEIGHT / 2);
    }

    private void enterIdle() {
        state = STATE_IDLE;
        getReadyPanel.show();
        gameOverPanel.G = false;
        scorePanel.G    = false;
        bird.playAnimation(1, true);
    }

    private void startPlaying() {
        state = STATE_PLAYING;
        bird.playAnimation(0, true);
        getReadyPanel.hide();
        spawnPipe();
    }

    private void spawnPipe() {
        if (pipeCount >= MAX_PIPES) return;
        int minGap  = groundY / 4;
        int maxGap  = groundY - groundY / 4;
        int gapCentre = MathTables.randomRange(minGap, maxGap);
        pipeX[pipeCount]      = VIRTUAL_WIDTH + PIPE_WIDTH;
        pipeGapY[pipeCount]   = gapCentre;
        pipePassed[pipeCount] = false;
        pipeCount++;
    }

    private void enterDying() {
        state = STATE_DYING;
        flashAlpha = 1.0f;
        flashTweener.start(1.0f, 0.0f, 4, 0.4f);
        pushEvent(8, 0);
        bird.die();
    }

    private void enterDead() {
        state = STATE_DEAD;
        gameOverPanel.show();

        int btnY    = (int) (VIRTUAL_HEIGHT * 0.70f);
        int halfGap = playButton.d / 2 + 4;
        playButton.setPosition(panelCenterX - halfGap - playButton.d, btnY);
        leaderboardButton.setPosition(panelCenterX + halfGap, btnY);
    }

    private void enterScore() {
        state = STATE_SCORE;
        scorePanel.show(score, bestScore,
                BRONZE_THRESHOLD, SILVER_THRESHOLD,
                GOLD_THRESHOLD, PLATINUM_THRESHOLD);
        pushEvent(12, 0);

        int btnY    = (int) (VIRTUAL_HEIGHT * 0.78f);
        int halfGap = playButton.d / 2 + 4;
        playButton.setPosition(panelCenterX - halfGap - playButton.d, btnY);
        leaderboardButton.setPosition(panelCenterX + halfGap, btnY);
    }

    private boolean birdHitsPipe(int bx, int by, int pipeIdx) {
        int halfGap   = PIPE_GAP / 2;
        int pLeft     = pipeX[pipeIdx];
        int pRight    = pLeft + PIPE_WIDTH;
        int bLeft     = bx - 7;
        int bRight    = bx + 7;
        int bTop      = by - 8;
        int bBot      = by + 8;
        if (bRight <= pLeft || bLeft >= pRight) return false;
        int topBot = pipeGapY[pipeIdx] - halfGap;
        int botTop = pipeGapY[pipeIdx] + halfGap;
        return bTop < topBot || bBot > botTop;
    }

    @Override
    public void tick(float deltaTime) {
        switch (state) {
            case STATE_IDLE:    tickIdle(deltaTime);    break;
            case STATE_PLAYING: tickPlaying(deltaTime); break;
            case STATE_DYING:   tickDying(deltaTime);   break;
            case STATE_DEAD:    tickDead(deltaTime);    break;
            case STATE_SCORE:   tickScore(deltaTime);   break;
        }
        drawWorld();
        super.tick(deltaTime);
    }

    private void tickIdle(float deltaTime) {
        bird.update(deltaTime);
        getReadyPanel.update(deltaTime);
        scrollGround();
        if (u > 0 && s[0] > 0) {
            startPlaying();
        }
    }

    private void tickPlaying(float deltaTime) {
        scrollGround();
        scrollPipes();

        if (u > 0) {
            bird.flap();
        }
        bird.update(deltaTime);

        int bx = bird.getX();
        int by = bird.getY();

        if (by + 10 >= groundY || by - 10 < skyTop) {
            enterDying();
            return;
        }

        for (int idx = 0; idx < pipeCount; idx++) {
            if (birdHitsPipe(bx, by, idx)) {
                enterDying();
                return;
            }
            if (!pipePassed[idx] && pipeX[idx] + PIPE_WIDTH < bx) {
                pipePassed[idx] = true;
                score++;
                if (score > bestScore) bestScore = score;
                pushEvent(7, 0);
                FloatingScore fs = (FloatingScore) m.next();
                if (fs != null) {
                    fs.F = true;
                    fs.G = true;
                    fs.j = bx;
                    fs.k = by - 30;
                    fs.d = 40;
                    fs.g = 0;
                    fs.h = 1;
                    fs.e = score;
                }
            }
        }

        gameTick++;
        pipeSpawnTimer++;
        if (pipeSpawnTimer >= pipeSpawnInterval && pipeCount < MAX_PIPES) {
            pipeSpawnTimer = 0;
            spawnPipe();
        }
    }

    private void tickDying(float deltaTime) {
        if (!flashTweener.g) {
            flashTweener.tick(deltaTime);
            flashAlpha = flashTweener.a;
        }
        bird.update(deltaTime);
        if (bird.isDead() && bird.getY() + 10 >= groundY) {
            enterDead();
        }
    }

    private void tickDead(float deltaTime) {
        gameOverPanel.update(deltaTime);
        playButton.update(deltaTime);
        leaderboardButton.update(deltaTime);
        if (playButton.h) {
            resetWorld();
            enterIdle();
            return;
        }
        if (leaderboardButton.h) {
            enterScore();
        }
    }

    private void tickScore(float deltaTime) {
        scorePanel.update(deltaTime);
        playButton.update(deltaTime);
        leaderboardButton.update(deltaTime);
        if (playButton.h) {
            resetWorld();
            enterIdle();
            return;
        }
        if (leaderboardButton.h) {
            pushEvent(5, 0);
        }
    }

    private void scrollGround() {
        groundOffset -= WORLD_SPEED;
        if (regionGround != null && groundOffset <= -regionGround.b) {
            groundOffset += regionGround.b;
        }
    }

    private void scrollPipes() {
        for (int idx = 0; idx < pipeCount; idx++) {
            pipeX[idx] -= WORLD_SPEED;
        }
        if (pipeCount > 0 && pipeX[0] + PIPE_WIDTH < 0) {
            for (int idx = 0; idx < pipeCount - 1; idx++) {
                pipeX[idx]      = pipeX[idx + 1];
                pipeGapY[idx]   = pipeGapY[idx + 1];
                pipePassed[idx] = pipePassed[idx + 1];
            }
            pipeCount--;
        }
    }

    private void drawWorld() {
        AtlasRegion bg = useNightBg ? regionBgNight : regionBgDay;
        if (bg != null) {
            int bgH = bg.c;
            int tilesNeeded = (VIRTUAL_HEIGHT / bgH) + 2;
            for (int tile = 0; tile < tilesNeeded; tile++) {
                drawSprite(bg, 0, tile * bgH, 1.0f);
            }
        }

        AtlasRegion pipeD = useAltPipe ? regionPipe2Down : regionPipeDown;
        AtlasRegion pipeU = useAltPipe ? regionPipe2Up   : regionPipeUp;
        for (int idx = 0; idx < pipeCount; idx++) {
            int gapCentre = pipeGapY[idx];
            int topPipeY  = gapCentre - PIPE_GAP / 2 - PIPE_HEIGHT;
            int botPipeY  = gapCentre + PIPE_GAP / 2;
            if (pipeD != null) drawSprite(pipeD, pipeX[idx], topPipeY, 1.0f);
            if (pipeU != null) drawSprite(pipeU, pipeX[idx], botPipeY, 1.0f);
        }

        if (regionGround != null) {
            int groundTiles = (VIRTUAL_WIDTH / regionGround.b) + 2;
            for (int tile = 0; tile < groundTiles; tile++) {
                drawSprite(regionGround, groundOffset + tile * regionGround.b, groundY, 1.0f);
            }
        }

        bird.draw(this);

        if (state == STATE_PLAYING || state == STATE_DYING) {
            scoreRenderer.drawCentered(this, score, panelCenterX, (int) (VIRTUAL_HEIGHT * 0.059f));
        }

        getReadyPanel.draw(this);
        gameOverPanel.draw(this);
        scorePanel.draw(this);

        m.draw(this);
        n.draw(this);

        if (flashAlpha > 0.0f && regionFlash != null) {
            drawSpriteScaledAt(regionFlash.i, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT,
                    regionFlash.d, regionFlash.e, regionFlash.f, regionFlash.g, flashAlpha);
        }

        if (state == STATE_DEAD || state == STATE_SCORE) {
            playButton.draw(this);
            leaderboardButton.draw(this);
        }
    }

    public void showTouchAt(int x, int y) {
    }
}
