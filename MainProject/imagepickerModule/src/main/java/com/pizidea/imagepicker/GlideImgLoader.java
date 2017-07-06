/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pizidea.imagepicker;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/**
 * <b>desc your class</b><br/>
 * Created by Eason.Lai on 2015/11/1 10:42 <br/>
 * contact：easonline7@gmail.com <br/>
 */
public class GlideImgLoader implements ImgLoader {
    @Override
    public void onPresentImage(ImageView imageView, String imageUri, int size) {
        Glide.with(imageView.getContext())
                .load(new File(imageUri))
//                .centerCrop()
                .dontAnimate()//没有动画直接显示图片
                .thumbnail(0.5f)//.thumbnail()方法的目的就是让用户先看到一个低解析度的图,点开后,再加载一个高解析度的图。
//                .override(size/4*3, size/4*3)
                .placeholder(R.drawable.default_img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.default_img)
                .into(imageView);

    }

}
