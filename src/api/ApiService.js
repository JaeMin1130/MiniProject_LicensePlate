import { API_BASE_URL } from "./api-config";

export function call(api, method, request) {
  // 로컬 스토리지에서 ACCESS TOKEN 가져오기
  const accessToken = localStorage.getItem("ACCESS_TOKEN");
 
  let headers = new Headers({
    "Content-Type": "multipart/form-data; boundary=<calculated when request is sent>",
  });

  if (accessToken && accessToken != null) {
    headers.append("Authorization", accessToken);
  }

  let options = {
    headers: headers,
    url: API_BASE_URL + api,
    method: method,
  };
  if (request) {
    // POST method
    options.body = JSON.stringify(request);
  }
  return fetch(options.url, options)
    .then((response) => {
      return response.json();
    })
    .catch((error) => {
    });
}