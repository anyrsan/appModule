package debug

import debug.model.BaseModel


/**
 *
 * @author any
 * @time 2019/05/23 17.39
 * @details
 */
class PostData(code: String?, message: String?, data: DataBean) : BaseModel<PostData.DataBean>(code, message, data) {
    class DataBean
}