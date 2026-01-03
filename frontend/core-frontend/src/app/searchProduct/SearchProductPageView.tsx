"use client";

import styles from "../searchProduct/page.module.css";
import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { Loan } from "@/models/Loan";
import { loanService } from "@/services/loanService";
import axios from "axios";
import { log } from "console";

type SearchProductResponse = {
  productNumber: string;
  publicId: string;
  productType: string;
};

export default function SearchProductPageView() {
  const router = useRouter();
  const [productNumber, setProductNumber] = useState("");
  const [documentId, setDocumentId] = useState("");
  const [customerCode, setcustomerCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [financialProducts, setFinancialProducts] = useState<SearchProductResponse[]>([]);


  
  const onSearch = async (e: React.FormEvent) => {

    e.preventDefault();
    setError(null);

    if (!productNumber.trim()) {
      alert("Ingrese un valor en los campos");
      setError("Ingrese un número de producto");
      return;
    }

    try {
      setLoading(true);
      const url = `http://localhost:8094/api/financial-product/${productNumber}`;

      const response = await axios.get<SearchProductResponse[]>(url);

      setFinancialProducts(response.data);
      setError(null);

      if (response.status == 200 && !response.data.length) {
        setError("No se encontraron resultados con tu busqueda");
      }
    } catch (err: unknown) {
    } finally {
      setLoading(false);
    }
  };

  const goToProductView = (financialProduct: SearchProductResponse) => {
    if (!financialProduct.publicId.length) {
      alert("Ha ocurrido un error al consultar este producto");
    }

    switch (financialProduct.productType.toUpperCase()) {
      case "LOAN":
        router.push(`loan/${financialProduct.publicId}`);
        break;

      default:
        alert("Hay que implementar esta vista");
        console.log("todo: hay que configurar esta vista");
        break;
    }
  };

  return (
    <div className="p-4">
      <h1>Buscar producto financiero</h1>
      <form className={styles.searchProductForm} onSubmit={onSearch}>
        <div className="flex flex-row gap-10">
          <div className="flex-row">
            <label htmlFor="productNumber">No. cuenta financiera: </label>
            <input
              id="productNumber"
              placeholder="Número de producto"
              value={productNumber}
              onChange={(e) => setProductNumber(e.target.value)}
            />
          </div>

          <div className="flex-row">
            <label htmlFor="documentId">Cedula, pasaporte o RNC: </label>
            <input
              id="documentId"
              placeholder="Cedula, pasaporte o RNC"
              value={documentId}
              onChange={(e) => setDocumentId(e.target.value)}
            />
          </div>
        </div>

        <button type="submit" disabled={loading} className={styles.searchBtn}>
          Buscar
        </button>

        {/* {error && <p className="error">{error}</p>} */}
      </form>

      {!loading && !error != null && financialProducts.length>0 && (
        <>
           <h2 className="text-xl font-bold text-black">
            Resultados
          </h2>
          <table className="min-w-full border-collapse">
            <thead className="sticky top-0 bg-gray-100 z-10">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-gray-600">
                  No. producto
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-gray-600">
                  Tipo
                </th>
              </tr>
            </thead>

            <tbody className="divide-y divide-gray-200 bg-red ">
              {financialProducts?.map((item, index) => (
                <tr
                  key={index}
                  className="hover:bg-blue-50 transition-colors cursor-pointer"
                  onClick={() => {
                    goToProductView(item);
                  }}
                >
                  <td className="clickeableBtn tableCell">{item.productNumber}</td>
                  <td className="tableCell">{item.productType}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}

      {!loading && error && <h2>{error}</h2>}
    </div>
  );
}
