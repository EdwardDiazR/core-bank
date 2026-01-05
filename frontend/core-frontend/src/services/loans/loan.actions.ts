"use server";

import { cookies } from "next/headers";

export async function selectLoan(loanPublicId:string) {

    (await cookies()).set("selectedProductPublicId",loanPublicId,{

    })
    
}

export async function clearSelectedLoan() {
  (await cookies()).delete("selectedProductPublicId");
}