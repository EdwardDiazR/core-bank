"use server"
import React from "react";
import { ClipboardList, FileTextIcon, Folder, ShieldCheck, Table } from "lucide-react";
import { MenuItem } from "./components/MenuItem";
import { LoanProvider } from "./LoanContext";
import { cookies, headers } from "next/headers";
import { loanService } from "@/services/loans/loanService";
import { LoanDto } from "@/models/LoanDto";
import { useRouter } from "next/router";
import { clearSelectedLoan } from "@/services/loans/loan.actions";

export default async function LoanDetailLayout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: { id: string };
}) {


const loanId = (await cookies()).get("selectedProductPublicId")?.value;


  console.log("LoanId desde Cookie: ", loanId);

  if (!loanId) {
    return;
  }

  const response = await loanService.getLoanByPublicId(loanId);
  const loan: LoanDto = response.data;
  // console.log(loan);


const MENU = [
  {
    label: "Detalle",
    href: `/loan/details`,
    tabIcon: "file",
  },
  {
    label: "Solicitudes de servicio",
    href: "#",
    tabIcon: "clipboard",
  },
  {
    label: "Pagos facturados",
    href: "#",
    tabIcon: "folder",
  },

  
  {
    label: "Tabla de amortización",
    href: `/loan/amortization`,
    tabIcon: "table",
  },
  {
    label: "Documentos",
    href: "#",
    tabIcon: "folder",
  },

  {
    label: "Garantías",
    href: "#",
    tabIcon: "shield",
  }
];
  return (
    <LoanProvider loan={loan}>
    <div className="flex h-full">
      {/* MENU LATERAL */}
      <aside className="w-50 shrink-0 bg-white border-r-2 border-gray-300 ">
        <nav className="bg-[#FFFFFF] h-full p-2 space-y-1 overflow-auto ">
          {MENU.map((item, index) => (
            <MenuItem key={index} 
            href={item.href} 
            label={item.label} 
            icon={item.tabIcon} />
          ))}
        </nav>
      </aside>

      {/* OUTLET */}
      <main className="flex-1 bg-gray-50 overflow-auto px-5">{children}</main>
    </div>
    </LoanProvider>
  );
}
