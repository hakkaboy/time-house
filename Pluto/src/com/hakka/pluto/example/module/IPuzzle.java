package com.hakka.pluto.example.module;

import org.andengine.entity.shape.IShape;

public interface IPuzzle {
	 int getTexturePositionX();
	
	 int getTexturePositionY();
	
	 void setAnswer(IShape ansArea);
	
	 IShape getAnswer();
}
