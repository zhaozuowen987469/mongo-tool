package cn.org.wangchangjiu.mongo.tool.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class FrontEndPagingRequest {

    @ApiModelProperty(value = "第几页;最小值:0", required = true)
    private Integer page;

    @ApiModelProperty(value = "一页展示多少行;最小值:1", required = true)
    private Integer size;

    @ApiModelProperty(value = "排序字段和升降序")
    private List<PagingSort.Order> orders;


    public FrontEndPagingRequest() {

        this(PagingRequest.Constants.PAGE, PagingRequest.Constants.SIZE, null);
    }

    public FrontEndPagingRequest(int page, int size) {

        this(page, size, null);
    }

    public FrontEndPagingRequest(int page, int size, List<PagingSort.Order> orders) {

        this.page = page;
        this.size = size;
        this.orders = orders;
    }

    public static class Constants {

        public static final Integer PAGE = 0;

        public static final Integer SIZE = 10;

        private Constants() {
        }
    }
}
