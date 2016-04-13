package com.game.particles;

import com.Config;
import com.game.shaders.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;


public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = Config.GAME_PATH+"particles/particleVShader.txt";
	private static final String FRAGMENT_FILE = Config.GAME_PATH+"particles/particleFShader.txt";

	private int location_modelViewMatrix;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	protected void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
