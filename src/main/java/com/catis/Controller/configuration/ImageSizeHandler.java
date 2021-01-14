package com.catis.Controller.configuration;

import com.chilkatsoft.CkBinData;
import com.chilkatsoft.CkCompression;

public class ImageSizeHandler {

	
	public String compress(String strBase64) {
		
	    CkCompression compress = new CkCompression();
	    compress.put_Algorithm("deflate");
	    
	    CkBinData binDat = new CkBinData();
	    // Load the base64 data into a BinData object.
	    // This decodes the base64. The decoded bytes will be contained in the BinData.
	    binDat.AppendEncoded(strBase64,"base64");
	 // Compress the BinData.
	    compress.CompressBd(binDat);
	
	    // Get the compressed data in base64 format:
	    String compressedBase64 = binDat.getEncoded("base64");
	    
	    return compressedBase64 ;
	}
	
	public String decompress(String compressedString) {	
		CkBinData binDat = new CkBinData();
		CkCompression compress = new CkCompression();
		 binDat.AppendEncoded(compressedString,"base64");
		    compress.DecompressBd(binDat);

		    String decompressedBase64 = binDat.getEncoded("base64");
		    
		    return decompressedBase64;
	}
    

    
}
