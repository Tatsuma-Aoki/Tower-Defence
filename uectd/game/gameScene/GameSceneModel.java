package uectd.game.gameScene;

import uectd.gameSystem.util.*;

import java.awt.event.*;
import java.util.ArrayList;

import uectd.game.ResourcePathDefines;
import uectd.game.gameScene.gameMain.BackGround;
import uectd.game.gameScene.gameMain.BallistaTurret;
import uectd.game.gameScene.gameMain.BaseEnemy;
import uectd.game.gameScene.gameMain.Tower;
import uectd.game.gameScene.gameMain.BaseTurret;
import uectd.game.gameScene.gameMain.CannonTurret;
import uectd.game.gameScene.gameMain.EnemyDieListener;
import uectd.game.gameScene.gameMain.EnemyParent;
import uectd.game.gameScene.gameMain.EnemySpawnerManager;
import uectd.game.gameScene.gameMain.EnemySpawnerParent;
import uectd.game.gameScene.gameMain.GameLevel;
import uectd.game.gameScene.gameMain.GraphBuilder;
import uectd.game.gameScene.gameMain.IMoneyTransfer;
import uectd.game.gameScene.gameMain.LaserTurret;
import uectd.game.gameScene.gameMain.FileReadSpawnStrategy;
import uectd.game.gameScene.gameMain.TowerArranger;
import uectd.game.gameScene.gameMain.TowerFallListener;
import uectd.game.gameScene.gameMain.TowerParent;
import uectd.game.gameScene.gameMain.TurretParent;
import uectd.game.gameScene.gameMain.TurretSocket;
import uectd.game.gameScene.gameMain.TurretSocketArranger;
import uectd.game.gameScene.gameMain.TurretSocketParent;
import uectd.game.gameScene.gameMain.enemy.Zombie;
import uectd.game.gameScene.gameMain.enemy.Goblin;
import uectd.game.gameScene.gameMain.enemy.Hobgoblin;
import uectd.game.gameScene.gameMain.enemy.Cyclopes;
import uectd.game.pauseScene.PauseScene;
import uectd.game.resultScene.ResultScene;
import uectd.gameSystem.Define;
import uectd.gameSystem.SceneChangeListener;
import uectd.gameSystem.SceneModel;

public class GameSceneModel extends SceneModel
        implements ActionListener, IMoneyTransfer, EnemyDieListener, TowerFallListener {

    public enum GameState {
        WaitingForNextWave, OnWave, GameOver, GameClear
    } // 内部クラスで状態を表すクラスを定義。状態遷移に用いる。

    private final float cameraZoomSpeed = 0.3f; // カメラの拡縮スピード
    private final float cameraZoomMax = 2.0f; // カメラ拡大の最大値
    private final float cameraZoomMin = 0.2f; // カメラ縮小の最小値

    private BackGround backGround; // 背景

    private Camera camera;
    private Vector2 cameraMoveDirection; // 正規化された、カメラが動くベクトルが入る変数
    private double cameraSpeed; // カメラの移動速度に緩急をつけるための変数
    private int cameraZoomOperation; // カメラの拡縮操作をControllerから受け取るための変数。範囲[-1,1]の整数が格納される。

    private GameTimer nextWaveTimer, waveEndTimer, gameOverTimer, gameClearTimer;
    // 次waveを待機するためのタイマー、wave開始から終了までのタイマー、ゲームオーバーとゲームクリア遷移まで待機するためのタイマー

    public ObservableComponent<GameState> currentState; // 現在のGameState

    public ObservableComponent<Integer> countdownNum, waveNum, numOfEnemies, balance, maxWaveNum, score;
    // wave開始前のカウントダウン値、現在ウェーブ数、マップ上の敵数、所持金、最大ウェーブ数(このウェーブを凌げばクリア)、スコア

    public ObservableComponent<ArrayList<BaseTurret>> availableTurretList; // 利用可能なタレット群
    public ObservableComponent<BaseTurret> selectingTurret; // 画面右にタレット情報を表示させるための変数
    public ObservableComponent<BaseTurret> purchasingTurret; // 購入対象のタレット

    private EnemySpawnerManager enemySpawnerManager; // waveごとにEnemySpawnerを配置するためのクラス

    public ObservableComponent<ArrayList<Tower>> targets; // 襲撃目標群

    // 新しく作成されるゲーム内オブジェクトが属する、ゲーム内オブジェクトのヒエラルキーの親
    private TurretParent turretParent; // タレットの親クラス
    private TowerParent towerParent; // タワーの親クラス
    private EnemySpawnerParent enemySpawnerParent; // スポナーの親クラス

    public GameSceneModel(SceneChangeListener sceneChangeListener) {
        super(sceneChangeListener);
        currentState = new ObservableComponent<GameState>(GameState.WaitingForNextWave);
        camera = new Camera(rootGameObject, rootGameObject, 1); // 初期のズーム比率は1
        addGameObject(camera);

        countdownNum = new ObservableComponent<>(10);
        waveNum = new ObservableComponent<>(1);
        maxWaveNum = new ObservableComponent<>(10);
        numOfEnemies = new ObservableComponent<>(0);
        balance = new ObservableComponent<>(2000); // 初期の所持金
        nextWaveTimer = new GameTimer(1000, this);
        cameraMoveDirection = new Vector2(0.0, 0.0);
        cameraSpeed = 160d;
        cameraZoomOperation = 0;
        camera.position = new Vector2(730, 100);
        camera.ratio = cameraZoomMin;

        backGround = new BackGround(rootGameObject, rootGameObject);
        addGameObject(backGround);
        EnemyParent enemyParent = new EnemyParent(rootGameObject, rootGameObject);
        addGameObject(enemyParent);
        TurretSocketParent turretSocketParent = new TurretSocketParent(rootGameObject, rootGameObject);
        addGameObject(turretSocketParent);
        turretParent = new TurretParent(rootGameObject, rootGameObject);
        addGameObject(turretParent);
        enemySpawnerParent = new EnemySpawnerParent(rootGameObject, rootGameObject);
        addGameObject(enemySpawnerParent);
        towerParent = new TowerParent(rootGameObject, rootGameObject);
        addGameObject(towerParent);
        GameLevel gameLevel = new GameLevel(rootGameObject, rootGameObject,
                GraphBuilder.build(ResourcePathDefines.UEC_MAP_GRAPH));
        addGameObject(gameLevel);

        selectingTurret = new ObservableComponent<>(null);
        purchasingTurret = new ObservableComponent<>(null);
        availableTurretList = new ObservableComponent<ArrayList<BaseTurret>>(new ArrayList<>());

        TurretSocketArranger.arrangeTurrets(ResourcePathDefines.TURRET_SOCKET_POSITION_PATH, rootGameObject,
                turretSocketParent, turretParent);

        targets = new ObservableComponent<>();
        targets.setValue(TowerArranger.arrange(ResourcePathDefines.TOWER_CANDIDATE_PATH, rootGameObject, towerParent,
                gameLevel.graph));
        for (Tower tower : targets.getValue()) {
            tower.addTowerFallListener(this);
        }

        enemySpawnerManager = new EnemySpawnerManager(gameLevel.graph, new FileReadSpawnStrategy(this),
                ResourcePathDefines.CANDIDATE_VERTEX_LIST_PATH, rootGameObject, rootGameObject, enemySpawnerParent,
                enemyParent, gameLevel, targets.getValue());

        score = new ObservableComponent<Integer>(0);

        nextWaveTimer.start();
    }

    public void pauseKeyPressed() {
        sceneChangeListener.scenePushed(new PauseScene(sceneChangeListener), null);
    }

    public void receiveIClickable(IClickable iClickable) {
        // 受け取ったものによって処理を変える。
        // nullならselectingTurretはnull, タレットならタレットそのもの、ソケットならselectingTurretを建設する(ことを試みる)
        // System.out.println(iClickable);
        if (iClickable == null) {
            var prevTurret = selectingTurret.getValue();
            if (prevTurret != null) {
                prevTurret.onUnSelected();
            }
            selectingTurret.setValue(null);
        } else if (iClickable instanceof BaseTurret) {
            var prevTurret = selectingTurret.getValue();
            if (prevTurret != null) {
                prevTurret.onUnSelected();
            }
            selectingTurret.setValue((BaseTurret) iClickable);
            ((BaseTurret) iClickable).onSelected();
        } else if (iClickable instanceof TurretSocket) {
            BaseTurret turret = purchasingTurret.getValue();
            if (turret != null && tryMoneyPay(turret.getPurchasePrice())) {
                ((TurretSocket) iClickable).tryBuildTurret(purchasingTurret.getValue().clone());
            }
        }
    }

    public void setPurchasingTurret(BaseTurret turret) {
        purchasingTurret.setValue(turret);
    }

    // カメラの移動方向ベクトルを設定するsetter。Controllerから移動方向ベクトルを受け取ることを想定している。
    public void setCameraMoveDirection(Vector2 cameraMoveDirection) {
        this.cameraMoveDirection = cameraMoveDirection;
    }

    // カメラのズーム比率を変更するための、キー入力から生成した-1,0,1の値を受け取るメソッド
    public void setCameraZoomOperation(int cameraZoomOperation) { // 引数のintの値は-1,0,1のどれか。
        this.cameraZoomOperation = cameraZoomOperation; // Modelで持っておきModel.calcメソッドで使う。
    }

    @Override
    public void start(Intent intent) {
        super.start(intent);
        var list = new ArrayList<BaseTurret>();
        if (intent.getBooleanValue("CannonTurret")) {
            list.add(new CannonTurret(rootGameObject, turretParent, this));
        } // 利用可能なタレットを登録していく
        if (intent.getBooleanValue("LaserTurret")) {
            list.add(new LaserTurret(rootGameObject, turretParent, this));
        }
        if (intent.getBooleanValue("BallistaTurret")) {
            list.add(new BallistaTurret(rootGameObject, turretParent, this));
        }
        availableTurretList.setValue(list);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var src = e.getSource();
        if (src == nextWaveTimer) { // Waiting -> OnWave
            int prevNum = countdownNum.getValue(); // ここでカウントダウン
            countdownNum.setValue(prevNum - 1);
            System.out.println("gsm.ap " + prevNum);
            if (prevNum <= 1) {
                nextWaveTimer.stop(); // 一旦nextWaveTimer止める
                currentState.setValue(GameState.OnWave); // State変更
                waveEndTimer = new GameTimer(30000, this); // 次タイマー用意
                enemySpawnerManager.arrange(waveNum.getValue()); // enemySpawnerManagerに対してwave数を渡し、スポナーを設置してもらう
                waveEndTimer.start(); // 今waveの時間切れタイマー開始
            } else if (prevNum == 4) {
                SoundManager.getInstance().play(ResourcePathDefines.COUNTDOWN_SOUND_PATH);
            }
        } else if (src == waveEndTimer) { // OnWave-> Waiting or OnWave-> GameClear
            // spawnerParentのchildをまとめて消せばおｋ
            if (waveNum.getValue() != maxWaveNum.getValue()) { // 次ウェーブが存在する場合
                waveNum.setValue(waveNum.getValue() + 1);
                waveEndTimer.stop();
                currentState.setValue(GameState.WaitingForNextWave);
                nextWaveTimer.stop();
                nextWaveTimer = new GameTimer(1000, this);
                countdownNum.setValue(10);
                nextWaveTimer.start();
            } else { // 次ウェーブが存在しない場合(ゲームクリア)
                waveEndTimer.stop();
                if (currentState.getValue() != GameState.GameOver) {
                    currentState.setValue(GameState.GameClear);
                    gameClearTimer = new GameTimer(5000, this);
                    gameClearTimer.start();
                    System.out.println("gameclear");
                    gameClearTimer.setRepeats(false);
                }
            }
        } else if (src == gameOverTimer) {
            // タワー全陥落の瞬間にGame Over!! 表示-> 5secくらい後にシーン遷移 みたいな感じ
            // 遷移先はリザルト画面とか？
            // タイトルに強制送還もアリかも
            Intent intent = new Intent();
            intent.setBooleanValue("Cleared", false);
            intent.setIntegerValue("Score", calcScore());
            SoundManager.getInstance().allStop();
            sceneChangeListener.sceneChanged(new ResultScene(sceneChangeListener), intent);
        } else if (src == gameClearTimer) {
            // ゲームクリア時の挙動。
            Intent intent = new Intent();
            intent.setBooleanValue("Cleared", true);
            intent.setIntegerValue("Score", calcScore());
            SoundManager.getInstance().allStop();
            sceneChangeListener.sceneChanged(new ResultScene(sceneChangeListener), intent);
            SoundManager.getInstance().allStop();
        }
    }

    @Override
    public void calc(float deltaTime) {
        // カメラの4方向移動
        camera.position.add(Vector2.scale(cameraMoveDirection, deltaTime * cameraSpeed / camera.ratio));
        camera.position.x = Math.max(Math.min(camera.position.x, 1.3 * Define.WINDOW_WIDTH),
                1.3 * -Define.WINDOW_WIDTH);
        camera.position.y = Math.max(Math.min(camera.position.y, 1.3 * Define.WINDOW_HEIGHT),
                1.3 * -Define.WINDOW_HEIGHT);
        // カメラの拡縮(昇降)
        var delta = deltaTime * cameraZoomSpeed * cameraZoomOperation; // dt * (カメラの昇降速度) *
                                                                       // (正または負に移動、もしくは動かないことを示す係数)
        camera.ratio = Math.max(Math.min(cameraZoomMax, camera.ratio + delta), cameraZoomMin); // clamp処理。超過時に上限下限に合わせる。
    }

    @Override
    public void moneyAdd(int amount) {
        balance.setValue(balance.getValue() + amount);
    }

    @Override
    public boolean tryMoneyPay(int amount) {
        if (balance.getValue() >= amount) {
            balance.setValue(balance.getValue() - amount);
            return true;
        }
        return false;
    }

    @Override
    public int getBalance() {
        return balance.getValue();
    }

    @Override
    public void onEnemyDead(BaseEnemy enemy) {
        moneyAdd(enemy.getDropValue());
        addScore(enemy.getDropScore());
    }

    @Override
    public void onTowerFall(Tower tower) {
        // タワー陥落でArraylistから削除。ArrayListが空になったら全タワー陥落
        targets.getValue().remove(tower);
        if (targets.getValue().isEmpty() && currentState.getValue() != GameState.GameClear) {
            currentState.setValue(GameState.GameOver);
            gameOverTimer = new GameTimer(5000, this);
            gameOverTimer.start();
            gameOverTimer.setRepeats(false);
            System.out.println("gameover");
        }
    }

    public void upTurretRangeLevel() {
        BaseTurret turret = selectingTurret.getValue();
        if (turret != null) {
            turret.upRangeLevel();
            selectingTurret.update();
        }
    }

    public void upTurretAttackLevel() {
        BaseTurret turret = selectingTurret.getValue();
        if (turret != null) {
            turret.upAttackLevel();
            selectingTurret.update();
        }
    }

    public void upTurretRofLevel() {
        BaseTurret turret = selectingTurret.getValue();
        if (turret != null) {
            turret.upRofLevel();
            selectingTurret.update();
        }
    }

    public void sellTurret() {
        BaseTurret turret = selectingTurret.getValue();
        if (turret != null) {
            turret.sell();
            selectingTurret.setValue(null);
        }
    }

    public void addScore(int additionalScore) {
        score.setValue(score.getValue() + additionalScore);
    }

    @Override
    public void pause() {
        super.pause();
        if (nextWaveTimer != null)
            nextWaveTimer.pause();
        if (waveEndTimer != null)
            waveEndTimer.pause();
        if (gameOverTimer != null)
            gameOverTimer.pause();
        if (gameClearTimer != null)
            gameClearTimer.pause();
    }

    @Override
    public void unpause() {
        super.unpause();
        if (nextWaveTimer != null)
            nextWaveTimer.unpause();
        if (waveEndTimer != null)
            waveEndTimer.unpause();
        if (gameOverTimer != null)
            gameOverTimer.unpause();
        if (gameClearTimer != null)
            gameClearTimer.unpause();
    }

    @Override
    public void stop() {
        super.stop();
        if (nextWaveTimer != null) {
            nextWaveTimer.stop();
        }
        if (waveEndTimer != null) {
            waveEndTimer.stop();
        }
        if (gameOverTimer != null) {
            gameOverTimer.stop();
        }
        if (gameClearTimer != null) {
            gameClearTimer.stop();
        }
    }

    private int calcScore() {
        return score.getValue() + (1000 * targets.getValue().size());
    }
}
