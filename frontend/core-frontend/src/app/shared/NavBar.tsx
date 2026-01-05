"use client";
import Link from "next/link";
import { useRouter } from "next/navigation";
import React from "react";

export default function NavBar() {
  const router = useRouter();

  const goToFinancialProductView = () => {
    router.push("/search-product");
  };
  return (
    <nav className="bg-white border-b border-gray-200 px-4 py-2.5 sm:px-6 sticky top-0 z-50 shadow-sm">
      <div className="max-w-7xl mx-auto flex flex-wrap justify-between items-center">
        {/* Logotipo / Branding */}
        {/* <div className="flex items-center">
          <span className="self-center text-xl font-bold whitespace-nowrap text-blue-900 tracking-tight">
            BANK<span className="text-blue-600">MODERN</span>
          </span>
        </div> */}

        {/* Enlaces de Navegación */}
        <div className="hidden md:flex items-center space-x-8">
          <ul className="flex flex-row space-x-8 font-medium text-sm">
            <li>
              <Link href="/" className="text-gray-600 hover:text-blue-700 transition-colors">
                Inicio
              </Link>
            </li>
            <li>
              <a href="#" className="text-gray-600 hover:text-blue-700 transition-colors">
                Clientes
              </a>
            </li>
            <li>
              <button
                onClick={goToFinancialProductView}
                className="text-gray-600 hover:text-blue-700 transition-colors focus:outline-none"
              >
                Producto financiero
              </button>
            </li>
          </ul>
        </div>

        {/* Acciones / CTA */}
        {/* <div className="flex items-center space-x-4">
          <button className="text-sm font-semibold text-gray-700 hover:text-blue-700 px-3 py-2">Banca en Línea</button>
          <button className="bg-blue-700 hover:bg-blue-800 text-white text-sm font-bold py-2 px-5 rounded-lg transition-all shadow-md active:scale-95">
            Abrir Cuenta
          </button>
        </div> */}
      </div>
    </nav>
  );
}
