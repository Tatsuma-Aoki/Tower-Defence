package uectd.gameSystem;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList;

import uectd.gameSystem.util.*;

public class GameObject implements Cloneable {
    protected GameObject root; // 木構造の根のGameObject
    public GameObject parent; // 同じく親
    public List<GameObject> children; // 同じく子

    private boolean enabled; // オブジェクトが有効であることを示すフラグ。有効な時のみ処理が行われる。
    public Vector2 position, offset; // オブジェクトの存在する座標の基準点とそこからずれる距離。相対座標を用いて表現した方が適切な場合はoffsetを用いる。
    public int depth; // 深さ。Z座標とも言い換えられる。

    private List<GameObject> addObjectList, removeObjectList; // 子オブジェクトを追加・削除する時のバッファ

    public Collider collider; // 当たり判定

    public GameObject(GameObject root, GameObject parent, Vector2 position) {
        this.enabled = true;
        this.root = root;
        this.parent = parent;
        this.position = position;
        this.offset = new Vector2();
        this.children = new ArrayList<>();
        this.addObjectList = new ArrayList<>();
        this.removeObjectList = new ArrayList<>();
    }

    public GameObject(GameObject root, GameObject parent) {
        this(root, parent, new Vector2());
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnabled();
        }
    }

    public void destroy() {
        this.enabled = false;
        if (parent != null)
            parent.removeChild(this);
        onDestroyed();
        for (var child : children) {
            child.destroy();
        }
    }

    public void onDestroyed() {
    }

    public void addChild(GameObject child) {
        addObjectList.add(child);
    }

    public void removeChild(GameObject child) {
        removeObjectList.add(child);
    }

    protected void _start() {
        start();
        if (enabled)
            onEnabled();

        for (GameObject gameObject : children) {
            if (gameObject.enabled)
                gameObject._start();
        }

        for (GameObject gameObject : addObjectList) {
            children.add(gameObject);
        }
        for (GameObject gameObject : removeObjectList) {
            children.remove(gameObject);
        }
        addObjectList.clear();
        removeObjectList.clear();
    }

    public void start() {
    }

    public void _calc(float deltaTime) {
        calc(deltaTime);

        for (GameObject gameObject : children) {
            if (gameObject.enabled)
                gameObject._calc(deltaTime);
        }

        for (GameObject gameObject : addObjectList) {
            children.add(gameObject);
        }
        for (GameObject gameObject : addObjectList) {
            gameObject.onSummoned();
        }
        for (GameObject gameObject : removeObjectList) {
            children.remove(gameObject);
        }
        addObjectList.clear();
        removeObjectList.clear();
    }

    protected void onSummoned() {
    }

    public void calc(float deltaTime) {
    }

    public void _pause() {
        pause();

        for (GameObject gameObject : children) {
            if (gameObject.enabled)
                gameObject._pause();
        }

        for (GameObject gameObject : addObjectList) {
            children.add(gameObject);
        }
        for (GameObject gameObject : removeObjectList) {
            children.remove(gameObject);
        }
        addObjectList.clear();
        removeObjectList.clear();
    }

    public void pause() {
    }

    public void _unpause() {
        unpause();

        for (GameObject gameObject : children) {
            if (gameObject.enabled)
                gameObject._unpause();
        }

        for (GameObject gameObject : addObjectList) {
            children.add(gameObject);
        }
        for (GameObject gameObject : removeObjectList) {
            children.remove(gameObject);
        }
        addObjectList.clear();
        removeObjectList.clear();
    }

    public void unpause() {
    }

    public void onEnabled() {
    }

    public <G> GameObject findChild(Class<G> class_) {
        for (GameObject child : children) {
            if (child.getClass().isAssignableFrom(class_)) {
                return child;
            }
        }
        return null;
    }

    public <G> GameObject findChildFromChildren(Class<G> class_) {
        var res = findChild(class_);
        if (res != null)
            return res;
        for (GameObject child : children) {
            GameObject go = child.findChild(class_);
            if (go != null)
                return go;
        }
        return null;
    }

    public <G> ArrayList<GameObject> findChildren(Class<G> class_) {
        var res = new ArrayList<GameObject>();
        for (GameObject child : children) {
            if (child.getClass().isAssignableFrom(class_)) {
                res.add(child);
            }
        }
        return res;
    }

    public <G> ArrayList<GameObject> findChildrenFromChildren(Class<G> class_) {
        var res = new ArrayList<GameObject>();

        Stack<GameObject> stack = new Stack<>();

        stack.push(this);
        while (!stack.empty()) {
            var currentGameObject = stack.pop();
            if (currentGameObject.getClass().isAssignableFrom(class_))
                res.add(currentGameObject);
            for (var nextGameObject : currentGameObject.children) {
                stack.push(nextGameObject);
            }
        }
        return res;
    }

    @Override
    public GameObject clone() {
        GameObject gameObject = null;

        try {
            gameObject = (GameObject) super.clone();
            gameObject.root = this.root;
            gameObject.parent = this.parent;
            gameObject.children = new ArrayList<>();
            gameObject.enabled = this.enabled;
            gameObject.addObjectList = new ArrayList<>();
            gameObject.removeObjectList = new ArrayList<>();
            gameObject.position = this.position.clone();
            gameObject.offset = this.offset.clone();
            if (gameObject.collider != null) {
                gameObject.collider = this.collider.clone();
                gameObject.collider.gameObject = gameObject;
            }
        } catch (CloneNotSupportedException ce) {
            ce.printStackTrace();
            FatalError.quit("オブジェクトのクローンに失敗しました");
        }
        return gameObject;
    }
}