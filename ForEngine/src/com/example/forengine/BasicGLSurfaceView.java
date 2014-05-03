/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.forengine;


import java.io.FileNotFoundException;

import android.content.Context;
import android.opengl.GLSurfaceView;

import android.opengl.GLES20;

class BasicGLSurfaceView extends GLSurfaceView {
	
	GLES20TriangleRenderer myRenderer;
	
    public BasicGLSurfaceView(Context context) throws FileNotFoundException {
        super(context);
        setEGLContextClientVersion(2);
        myRenderer = new GLES20TriangleRenderer(context);
        //setRenderer(new GLES20TriangleRenderer(context));
        setRenderer(myRenderer);
    }
}
