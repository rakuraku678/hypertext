package utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.cloudinary.Cloudinary;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import play.libs.Codec;
import play.libs.WS;
import play.mvc.Router;
import play.mvc.Scope.RenderArgs;
import play.vfs.VirtualFile;
import sun.misc.BASE64Decoder;

public class CloudinaryUtils {

	public static final String SECRET_KEY = "YSpj1WCTmo61E__olPxNoZ2ThGc";
	public static final String API_KEY = "469785936595817";
	public static final String CLOUD_NAME = "hyvrprjak";
	public static final String CLOUDINARY_DOMAIN = "http://res.cloudinary.com/";
	public static final String relativeCorsFile = "public/javascripts/cloudinary/cloudinary_cors.html";

	public static final String FETCH = "/image/fetch/";
	public static final String UPLOAD = "/image/upload/";
	public static final String[] EMPTY_IMAGE_ENDS_WITH = new String[] { FETCH, UPLOAD };

	public static void prepareCloudinaryUploadImageParams(String paramsSuffix) {
		long timestamp = new Date().getTime();
		String callback = Router.reverse(VirtualFile.fromRelativePath(relativeCorsFile), true);
		StringBuilder stringToSign = new StringBuilder("callback=");
		stringToSign.append(callback).append("&").append("timestamp=").append(Long.toString(timestamp))
				.append(SECRET_KEY);
		String signature = Codec.hexSHA1(stringToSign.toString());

		RenderArgs.current().put("cloudName", CLOUD_NAME);
		RenderArgs.current().put("apiKey", API_KEY);

		RenderArgs.current().put("apiKey" + paramsSuffix, API_KEY);
		RenderArgs.current().put("cloudName" + paramsSuffix, CLOUD_NAME);
		RenderArgs.current().put("timestamp" + paramsSuffix, timestamp);
		RenderArgs.current().put("signature" + paramsSuffix, signature);
		RenderArgs.current().put("callback" + paramsSuffix, callback);
	}

	public static Map uploadFileToCloudinary(File file, String imageDomain) throws IOException {
		Map<String, String> config = Maps.newHashMap();
		config.put("cloud_name", CLOUD_NAME);
		config.put("api_key", API_KEY);
		config.put("api_secret", SECRET_KEY);
		Cloudinary cloudinary = new Cloudinary(config);

		Map uploadResults = cloudinary.uploader().upload(file, Cloudinary.emptyMap());
		if (uploadResults.containsKey("url") && !StringUtils.isNotEmpty(imageDomain)) {
			String newImageUrl = StringUtils.replace(uploadResults.get("url").toString(), CLOUDINARY_DOMAIN,
					imageDomain);
			uploadResults.put("url", newImageUrl);
		}
		return uploadResults;
	}

	public static String changeImage(String imgUrl, String change) {
		if (Strings.isNullOrEmpty(imgUrl)) {
			return "";
		}
		return imgUrl.replaceAll("upload/", "upload/" + change + "/");
	}

	public static String getUrl(String image, String transformations, String defaultImageUrlToFetch) {
		if (StringUtils.isEmpty(image) || StringUtils.endsWithAny(image, EMPTY_IMAGE_ENDS_WITH)) {
			if (StringUtils.isEmpty(defaultImageUrlToFetch)) {
				return null;
			} else {
				return CLOUDINARY_DOMAIN + CLOUD_NAME + FETCH + StringUtils.trimToEmpty(transformations) + "/"
						+ defaultImageUrlToFetch;
			}
		} else if (StringUtils.contains(image, FETCH)) {
			return image.replaceFirst(FETCH, FETCH + StringUtils.trimToEmpty(transformations) + "/");
		} else if (StringUtils.contains(image, UPLOAD)) {
			return image.replaceFirst(UPLOAD, UPLOAD + StringUtils.trimToEmpty(transformations) + "/");
		} else {
			return CLOUDINARY_DOMAIN + CLOUD_NAME + UPLOAD + StringUtils.trimToEmpty(transformations) + "/" + image;
		}
	}

	public static Map fetchUrlAndUploadToCloudinary(String url) throws IOException {
		InputStream inputStream = WS.url(url).get().getStream();
		BufferedImage image = ImageIO.read(inputStream);
		File picFile = new File(UUID.randomUUID() + ".jpg");
		ImageIO.write(image, "jpg", picFile);
		Map uploadDetails = CloudinaryUtils.uploadFileToCloudinary(picFile, StringUtils.EMPTY);
		picFile.delete();
		return uploadDetails;
	}

	public static File getFileFromBase64(String base64pic, String fileName) throws IOException {
		File picFile = new File(fileName + "-.jpg");
		picFile.mkdirs();
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] imageByte = decoder.decodeBuffer(base64pic);
		ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
		BufferedImage image = ImageIO.read(bis);
		bis.close();
		ImageIO.write(image, "jpg", picFile);
		return picFile;
	}

}
