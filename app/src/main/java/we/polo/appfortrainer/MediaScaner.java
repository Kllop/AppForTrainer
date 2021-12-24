package we.polo.appfortrainer;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MediaScaner implements MediaScannerConnectionClient {
    private String path;
    private MediaScannerConnection mMs;

    public MediaScaner(Context context, String p){
        path = p;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(path, null);
    }

    @Override
    public void onScanCompleted(String s, Uri uri) {
        mMs.disconnect();
    }
}
