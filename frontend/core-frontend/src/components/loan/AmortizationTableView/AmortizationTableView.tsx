"use client";
import { AmortizationTable } from "@/models/AmortizationTable";
import { formatNumberWithCurrency } from "@/utils/numberFormatter";
import axios from "axios";
import React, { useState } from "react";
import styles from "./page.module.css";
import IframeModal from "./IframeModal";
import { useLoan } from "../../../app/loan/view/LoanContext";


export const AmortizationTableView = () => {

  const loan = useLoan();
  const currency = loan.currency;
  const publicId = loan.publicId;

  const amortizationTableUrl = `http://localhost:8094/loan/amortization/${loan.publicId}/pdf`;
  const [blobPdfUrl, setBlobPdfUrl] = useState<string|null>(null);

  const generateTablePdf = async () => {
    // window.open(amortizationTableUrl, "_blank");
    if (!blobPdfUrl?.length) {
      const response = await fetch(`http://localhost:8094/loan/amortization/${publicId}/pdf`);
      const blob = await response.blob();
      setBlobPdfUrl(URL.createObjectURL(blob));
    }
  };


  return (
    <div className="flex-1 p-5 ">
      <div className={`py-2  ${styles.pdfHeader}`}>
        <h2>Tabla de amortizacion</h2>
        <div className="flex justify-end">
          
          <IframeModal buttonLabel="Generar pdf" src={blobPdfUrl} generate={generateTablePdf} />
          {/* <button onClick={generateTablePdf} className={styles.generatePdfBtn}>
            Generar PDF
          </button> */}
        </div>
      </div>

      <div className="h-[550px] overflow-y-auto rounded-lg border border-gray-200 shadow-sm">
        <table className="min-w-full border-collapse">
          <thead className="sticky top-0 bg-[#E6F0FF] z-10">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-gray-600">
                Pago No.
              </th>
               <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-gray-600">
                Fecha
              </th>
              <th className="px-4 py-3 text-center text-xs font-semibold uppercase tracking-wide text-gray-600">
                Monto Cuota
              </th>
              <th className="px-4 py-3 text-center text-xs font-semibold uppercase tracking-wide text-gray-600">
                Capital
              </th>
              <th className="px-4 py-3 text-center text-xs font-semibold uppercase tracking-wide text-gray-600">
                Interés
              </th>
              <th className="px-4 py-3 text-center text-xs font-semibold uppercase tracking-wide text-gray-600">
                Saldo
              </th>
            </tr>
          </thead>

          <tbody className="divide-y divide-gray-200 bg-white">
            {loan.amortizationTable?.items?.map((item, index) => (
              <tr key={index} className="hover:bg-blue-50 transition-colors cursor-pointer">
                <td className="px-4 py-2 text-sm text-gray-800">{item.installmentNumber}</td>
                <td className="px-4 py-2 text-sm text-gray-800">{item.paymentDate.toLocaleString()}</td>

                <td className="px-4 py-2 text-sm text-gray-800 text-center">
                  {formatNumberWithCurrency(item.cuota, currency)}
                </td>

                <td className="px-4 py-2 text-sm text-gray-800 text-center">
                  {formatNumberWithCurrency(item.capital, currency)}
                </td>

                <td className="px-4 py-2 text-sm text-gray-800 text-center">
                  {formatNumberWithCurrency(item.interes, currency)}
                </td>

                <td className="px-4 py-2 text-sm font-medium text-gray-900 text-center">
                  {formatNumberWithCurrency(item.saldo, currency)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
