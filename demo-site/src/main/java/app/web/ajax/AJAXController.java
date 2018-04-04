package app.web.ajax;

import core.framework.json.JSON;
import core.framework.web.Request;
import core.framework.web.Response;

/**
 * @author neo
 */
public class AJAXController {
    public Response ajax(Request request) {
        Bean bean = request.bean(Bean.class);
        return Response.text("posted: " + JSON.toJSON(bean));
    }
}
