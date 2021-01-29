package com.catis.Controller.configuration;
/**
 * import com.chilkatsoft.CkBinData;
 * import com.chilkatsoft.CkCompression;
 * <p>
 * public class ImageSizeHandler {
 * <p>
 * <p>
 * public static String compress(String strBase64) {
 * <p>
 * CkCompression compress = new CkCompression();
 * compress.put_Algorithm("deflate");
 * <p>
 * CkBinData binDat = new CkBinData();
 * // Load the base64 data into a BinData object.
 * // This decodes the base64. The decoded bytes will be contained in the BinData.
 * binDat.AppendEncoded(strBase64,"base64");
 * // Compress the BinData.
 * compress.CompressBd(binDat);
 * <p>
 * // Get the compressed data in base64 format:
 * String compressedBase64 = binDat.getEncoded("base64");
 * <p>
 * return compressedBase64 ;
 * }
 * <p>
 * public static String decompress(String compressedString) {
 * CkBinData binDat = new CkBinData();
 * CkCompression compress = new CkCompression();
 * binDat.AppendEncoded(compressedString,"base64");
 * compress.DecompressBd(binDat);
 * <p>
 * String decompressedBase64 = binDat.getEncoded("base64");
 * <p>
 * return decompressedBase64;
 * }
 * }
 */
