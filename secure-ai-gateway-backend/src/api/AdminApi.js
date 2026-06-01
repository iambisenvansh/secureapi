import api from "./httpClient";

export const getSummary = () => {
  return api.get("/admin/analytics/summary");
};

export const getRecentLogs = () => {
  return api.get("/admin/analytics/recent");
};

export const getByEndpoint = () => {
  return api.get("/admin/analytics/by-endpoint");
};
