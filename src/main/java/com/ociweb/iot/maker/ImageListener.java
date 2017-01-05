package com.ociweb.iot.maker;

import java.io.File;

/**
 * Listener for responding to image-receive events on a hardware system
 * equipped with a camera or other image-generating systems.
 *
 * @author Brandon Sanders [brandon@alicorn.io]
 */
@FunctionalInterface
public interface ImageListener {

    /**
     * Invoked when a new image is received from the hardware.
     *
     * The file reference passed to this method will never be
     * invalidated, and the image file represented by the
     * reference will never be deleted. Therefore, if many images
     * are being created, it's the responsibility of this listener
     * to delete the image files as needed.
     *
     * @param image File reference to the received image.
     */
    void onImage(File image);
}
