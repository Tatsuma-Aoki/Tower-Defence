package uectd.game.gameScene.gameMain;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.CircleCollider;
import uectd.gameSystem.util.IClickable;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.Sprite;

public class TurretSocket extends Sprite implements IClickable {

    private TurretParent turretParent; // 配置されたタレットが属するゲーム内オブジェクトのヒエラルキーの親
    private BaseTurret turret; // タレットを持つ。配置されていない間はnullとする
    // GameSceneModel.selectingTurretをprivateで持っておく。ソケットクリック時にselectingTurrte

    // GOのコンストラクタGO(root,parent)なのでrootからfindchildしてなんとかTPを発見してこのコンストラクタに投げてあげればおｋ
    public TurretSocket(GameObject root, GameObject parent, TurretParent turretParent) {
        super(root, parent);
        this.turret = null;
        this.setImage(ImageManager.getInstance().getImage(ResourcePathDefines.TURRET_SOCKET_IMAGE));
        this.turretParent = turretParent;
        this.depth = 5;
        this.collider = new CircleCollider(this, this.image.getHeight(null) / 2);
    }

    // 建築。もしソケットにタレットがあれば建築はできない。
    public boolean tryBuildTurret(BaseTurret turret) {
        if (this.turret == null || !(this.turret.getEnabled())) { // タレットがない or 売却済みによりenabled==falseの場合、タレットは建設可能
            this.turret = turret;
            turret.position = this.position.clone();
            turret.parent = turretParent;
            turretParent.addChild(turret);
            return true;
        } else { // この条件に当てはまらないと建設はできない
            return false;
        }
    }

}