import { cookies } from "next/headers";
import axios from "axios";
import { log } from "console";
import { NextResponse } from "next/server";

export async function GET(req: Request, { params }: { params: { loanPublicId: string } }) {
  const { loanPublicId } = await params;

  const cookieStore = cookies();
  const userId = (await cookieStore).get("user_id")?.value;
  console.log(userId);
  // ;(await cookies()).delete("test")
  console.log((await cookies()).getAll());
  // if (!userId) {
  //     console.log("aquii")

  //   return NextResponse.json(
  //     { message: "Usuario no autenticado" },
  //     { status: 401 }
  //   );
  // }

  console.log("➡️ Entró al API loan/[id]");
  console.log("PublicId:", loanPublicId);

  const response = await axios.get(`http://localhost:8094/api/v1/loan/${loanPublicId}`, {
    headers: {
      cookie: req.headers.get("cookie") ?? "",
    },
    withCredentials: true,
  });

  return NextResponse.json(response.data.data);
}
