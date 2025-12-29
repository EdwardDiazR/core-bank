"use client"
import { useRouter } from "next/navigation";
import React from "react";

export default function NavBar() {

  const router = useRouter();

  const goToFinancialProductView = ()=>{
    router.push("/search")
  }
  return (
    <nav className="navBarHeader">
      <ul className="">
        <li>Inicio</li>
        <li>Clientes</li>
        <li>
          <button className="" onClick={goToFinancialProductView}>Producto financiero</button>
        </li>
      </ul>
    </nav>
  );
}
