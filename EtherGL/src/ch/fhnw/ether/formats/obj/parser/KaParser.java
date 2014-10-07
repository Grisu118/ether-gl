package ch.fhnw.ether.formats.obj.parser;

import ch.fhnw.ether.formats.obj.Material;
import ch.fhnw.util.color.RGB;

public class KaParser extends LineParser {
	RGB ka = null;

	@Override
	public void incoporateResults(WavefrontObject wavefrontObject) {
		Material currentMaterial = wavefrontObject.getCurrentMaterial();
		currentMaterial.setKa(ka);

	}

	@Override
	public void parse() {
		try {
			ka = new RGB(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3]));
		} catch (Exception e) {
			throw new RuntimeException("VertexParser Error");
		}
	}

}
