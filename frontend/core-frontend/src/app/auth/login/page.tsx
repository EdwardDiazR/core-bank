"use client";
import { useRouter } from "next/navigation";
import React from "react";

export default function LoginPage() {
  const router = useRouter();
  const login = (e:React.FormEvent) => {
    e.preventDefault()
    router.push("/searchProduct");
  };

  return (
    <main className="flex flex-1 flex-col w-screen h-screen bg-blue-500 justify-center-safe items-center ">
      <form
        className="flex flex-col bg-white p-5 justify-center items-center w-xl gap-y-2 rounded-md shadow-lg-black-800"
        onSubmit={login}
      >
        <div className="flex flex-1 flex-col py-2 gap-y-1">
          <label>Usuario:</label>
          <input placeholder="Usuario" type="text" />
        </div>
        <div className="flex flex-col py-2 gap-y-1">
          <label>Contraseña</label>
          <input placeholder="Contraseña" />
        </div>
        <button className="bg-sky-500 rounded-lg p-1 px-5   text-white flex flex-1 cursor-pointer hover:bg-sky-700 focus:outline-5 " type="submit">
          Iniciar sesion
        </button>
      </form>
    </main>
  );
}
