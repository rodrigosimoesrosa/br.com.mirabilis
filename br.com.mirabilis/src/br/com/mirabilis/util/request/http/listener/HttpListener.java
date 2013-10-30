package br.com.mirabilis.util.request.http.listener;

import br.com.mirabilis.util.request.ResponseData;

public interface HttpListener<InputStrem> {
	public void onResponseData(ResponseData<InputStrem> data);
}
