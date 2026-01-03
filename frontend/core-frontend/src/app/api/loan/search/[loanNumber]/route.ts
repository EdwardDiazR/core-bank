"use server"

import { cookies } from "next/headers";
import { NextResponse } from "next/server";
import axios from "axios";

export async function GET(
  _: Request,
  { params }: { params: { loanNumber: string } }
) {
  const cookieStore = cookies();
  const userId = (await cookieStore).get("user_id")?.value;
  const { loanNumber } = await params;

//   if (!userId) {
//       console.log("aquii")

//     return NextResponse.json(
//       { message: "Usuario no autenticado" },
//       { status: 401 }
//     );
//   }

  console.log("aquii")
  const response = await axios.get(
    `http://localhost:8094/api/v1/loan/search/${loanNumber}`,
    {
      withCredentials:true
    }
  );

  console.log(response.data.data)
  return NextResponse.json(response.data.data);
}
