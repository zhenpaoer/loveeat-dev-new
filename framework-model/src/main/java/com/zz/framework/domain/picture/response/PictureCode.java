package  com.zz.framework.domain.picture.response;
import com.google.common.collect.ImmutableMap;
import com.zz.framework.common.model.response.ResultCode;
import com.zz.framework.domain.business.response.BusinessCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by admin on 2020/5/15.
 * 1000开头
 */
@ToString
public enum PictureCode implements ResultCode {
	PICTURE_CREATE_FALSE(false,2001,"添加图片失败！"),
	PICTURE_NOTEXIT(false,2002,"图片不存在，请重试！"),
	PICTURE_UPLOAD_WRONGPARAMS(false,2003,"上传失败:【查询参数错误】"),
	PICTURE_UPLOAD_ERROR(false,2004,"上传失败"),
	PICTURE_DOWNLOAD_WRONGPARAMS(false,2005,"下载失败:【查询参数错误】"),
	PICTURE_DOWNLOAD_ERROR(false,2006,"下载失败"),
	PICTURE_HAS_EXIT(false,2007,"图片不存在，请重试！");
//	PICTURE_CHECK_ID_FALSE(false,2002,"id校验失败，请重试！"),
//	PICTUREDETAIL_CREATE_FALSE(false,2004,"添加商家分店信息失败！"),
//	PICTUREDETAIL_UPDATE_FALSE(false,2005,"修改商家分店信息失败！");

	//操作代码
	@ApiModelProperty(value = "操作是否成功", example = "true", required = true)
	boolean success;

	//操作代码
	@ApiModelProperty(value = "操作代码", example = "22001", required = true)
	int code;
	//提示信息
	@ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
	String message;
	private PictureCode(boolean success, int code, String message){
		this.success = success;
		this.code = code;
		this.message = message;
	}
	private static final ImmutableMap<Integer, PictureCode> CACHE;

	static {
		final ImmutableMap.Builder<Integer, PictureCode> builder = ImmutableMap.builder();
		for (PictureCode pictureCode : values()) {
			builder.put(pictureCode.code(), pictureCode);
		}
		CACHE = builder.build();
	}

	@Override
	public boolean success() {
		return success;
	}

	@Override
	public int code() {
		return code;
	}

	@Override
	public String message() {
		return message;
	}
}
