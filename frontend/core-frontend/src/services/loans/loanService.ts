
import { http } from "../http";

const BASE_URL = "/api/loan";

export const loanService = {
  getLoanByNumber: async (loanNumber: string) => {
    return await http.get(`/api/loan/search/${loanNumber}`);
  },

  getLoanByPublicId: async (loanPublicId: string) => {
    console.log("BY PUBLICID")
    return await http.get(`/api/loan/${loanPublicId}`);
  },
};
