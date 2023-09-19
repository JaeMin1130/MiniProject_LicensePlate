let backendHost;

const hostname = window && window.location && window.location.hostname;

backendHost = "http://10.125.121.174:8080";
// backendHost = "http://localhost:8080";

export const API_BASE_URL = `${backendHost}`;