"use client";

import { useEffect, useState } from "react";
import { AmortizationTable } from "../models/AmortizationTable";
import axios, { AxiosResponse } from "axios";
import { Loan } from "../models/Loan";
import { cookies } from "next/headers";
import { http } from "@/services/http";

export default function Home() {
  

  useEffect(()=>{

  },[])

  return (
    <>
    <button onClick={()=>{  
      const setCookie = http.get("api/auth/login",{withCredentials:true}).then((r) => {
    console.log(r);
  }).catch((err:unknown)=>console.log(err));
}}>Agregar cookie</button></>
    // <div className="min-w-full bg-white">
    //   <main className="min-h-full bg-white">
    //     <h1 className="text-black">Tabla de amortizacion</h1>
    //     <div className="h-[550px] overflow-y-auto  border-gray-200 rounded-lg shadow-md">
    //       <table className="min-w-full">
    //         <thead className="bg-gray-100">
    //           <tr>
    //             <th className="px-4 py-2 text-left text-gray-700 font-medium">Pago No.</th>
    //             <th className="px-4 py-2 text-left text-gray-700 font-medium">Monto cuota</th>
    //             <th className="px-4 py-2 text-left text-gray-700 font-medium">Capital</th>
    //             <th className="px-4 py-2 text-left text-gray-700 font-medium">Interes</th>
    //             <th className="px-4 py-2 text-left text-gray-700 font-medium">Mora</th>
    //             <th className="px-4 py-2 text-left text-gray-700 font-medium">Saldo</th>
    //           </tr>
    //         </thead>
    //         <tbody className="bg-white divide-y divide-gray-200">
    //           {table &&
    //             table.item &&
    //             table.item.map((item, index) => {
    //               console.log(item);
    //               return (
    //                 <tr key={index} className="hover:bg-gray-50 transition-colors">
    //                   <td className="px-4 py-2 text-black">{item.installmentNumber}</td>
    //                   <td className="px-4 py-2 text-black">{formatNumber(item.cuota)}</td>
    //                   <td className="px-4 py-2 text-black">{formatNumber(item.capital)}</td>
    //                   <td className="px-4 py-2 text-black">{formatNumber(item.interes)}</td>
    //                   <td className="px-4 py-2 text-black">{formatNumber(item.lateFeeAmount)}</td>
    //                   <td className="px-4 py-2 text-black">{formatNumber(item.saldo)}</td>
    //                 </tr>
    //               );
    //             })}
    //         </tbody>
    //       </table>
    //     </div>
    //     {loan &&<>
    //     <div>
    //       <h2>{loan.number}</h2>
    //     </div></>}
    //   </main>
    // </div>
  );
}
