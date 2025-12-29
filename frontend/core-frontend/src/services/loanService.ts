import axios from "axios";

const BASE_URL = "http://localhost:8094/api/v1/loan";
export const getLoanByNumber = (loanNumber: string) => {
  return axios.get(`${BASE_URL}/search/${loanNumber}`);
};

export const getLoanByPublicId = (loanPublicId: string) => {
  return axios.get(`${BASE_URL}/${loanPublicId}`);
};

