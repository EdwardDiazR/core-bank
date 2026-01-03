import axios from "axios";
import { NextResponse } from "next/server";

export async function GET() {
  const response = await axios.post("http://localhost:8094/auth/set-cookie", {}, { withCredentials: true });
  console.log(response.data);

  return NextResponse.json(response.data);
}
