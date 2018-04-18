package classes.behavior;

import com.sun.media.jfxmediaimpl.MediaDisposer;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceDisposer {
    private List<MediaDisposer.Disposable> resources;
    private static ResourceDisposer disposer;

    public synchronized static ResourceDisposer getInstance() {
        if (disposer == null)
            disposer = new ResourceDisposer();
        return disposer;
    }

    private ResourceDisposer() {
        resources = Collections.synchronizedList(new ArrayList<>());
    }

    public void add(MediaDisposer.Disposable disposable) {
        resources.add(disposable);
    }

    public void disposeAll() {
        resources.forEach(MediaDisposer.Disposable::dispose);
        resources.clear();
    }
}
