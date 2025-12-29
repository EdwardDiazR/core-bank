"use client";

import styles from "../searchProduct/page.module.css";
import React, { useState } from "react";
import { useRouter } from "next/navigation";
import * as loanService from "@/services/loanService";
import { Loan } from "@/models/Loan";

export default function SearchProductPageView() {
  const router = useRouter();
  const [productNumber, setProductNumber] = useState("");
  const [documentId, setDocumentId] = useState("");
  const [customerCode, setcustomerCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [financialProducts, setFinancialProducts] = useState<Loan[]>([]);

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

      const response = await loanService.getLoanByNumber(productNumber);
      const publicId = response.data.data.publicId;

      if (!publicId) {
        throw new Error("Producto sin identificador público");
      }

      if (financialProducts.length) {
        setFinancialProducts([]);
      }
      financialProducts.push(response.data.data);

      // 👉 navegación segura con ID opaco
      //router.push(`/loan/${publicId}`);
    } catch (err: unknown) {
      setError("No se encontró el producto");
    } finally {
      setLoading(false);
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

      <h2>Resultados de busqueda:</h2>
      <table>
        <thead>
          <tr>
            <th>No. Producto</th>
            <th>Estatus</th>
             <th>Condicion de firma</th>
          </tr>
        </thead>
        <tbody>
          {financialProducts.map((fp, i) => {
            return (
              <tr key={i} className={styles.searchResultItem} onClick={() => router.push(`loan/${fp.publicId}`)}>
                <td>
                  <button className={styles.searchResultBtn} onClick={() => router.push(`loan/${fp.publicId}`)}>
                    {fp.number}
                  </button>
                </td>
                <td>
                  <span>{fp.status}</span>
                </td>

                 <td>
                  <span>{fp.signType}</span>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
      <ul></ul>
    </div>
  );
}
