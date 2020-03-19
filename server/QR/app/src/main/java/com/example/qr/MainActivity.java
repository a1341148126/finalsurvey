package com.example.qr;

import java.util.Hashtable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //encodeQRCode();

        qr_image = (ImageView) findViewById(R.id.qr_image);
        qr_text = (EditText) findViewById(R.id.qr_text);
        Button btn = (Button) findViewById(R.id.qr_create_image);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                encodeQRCode();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private final static int QR_WIDTH = 200, QR_HEIGHT = 200;
    String TAG ="abc";
    ImageView qr_image =null;
    EditText qr_text = null;
    TextView qr_result = null;

    private void encodeQRCode() {
        try {
            // 读取输入的String
            String text = qr_text.getText().toString();
            Log.i(TAG, "生成的文本：" + text);
            if (text == null || "".equals(text) || text.length() < 1) {
                return;
            }
            // android二维码的编码与解码（图片解码与摄像头解码）

            // 实例化二维码对象
            QRCodeWriter writer = new QRCodeWriter();
            // 用一个map保存编码类型
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            // 保持字符集为“utf－8”
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            /*
             * 第一个参数：输入的文本
             * 第二个参数：条形码样式－》二维码
             * 第三个参数：宽度
             * 第四个参数：高度
             * 第五个参数：map保存编码类型
             */
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT, hints);
            System.out.println("w:" + bitMatrix.getWidth() + "h:"
                    + bitMatrix.getHeight());
            // 将像素保存在数组里
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {// 二维码黑点
                        pixels[y * QR_HEIGHT + x] = 0xff000000;
                    } else {// 二维码背景白色
                        pixels[y * QR_HEIGHT + x] = 0xffffffff;
                    }
                }
            }

            // 生成位图
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            /*
             * 第一个参数：填充位图的像素数组
             * 第二个参数：第一个颜色跳过几个像素读取
             * 第三个参数：像素的幅度
             * 第四个参数：起点x坐标
             * 第五个参数：起点y坐标
             * 第六个参数：宽
             * 第七个参数：高
             */
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            // 显示图片
            qr_image.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}