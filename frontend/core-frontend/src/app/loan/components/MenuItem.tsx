"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import clsx from "clsx";
import { Clipboard, ClipboardClock, CreditCard, FileText, Folder, Info, ShieldCheck, Table } from "lucide-react";
import { COLORS } from "@/utils/colors";

const ICONS: Record<string, React.ElementType> = {
  file: Info,
  clipboard:Clipboard,
  table: ClipboardClock,
  folder: Folder,
  payment: CreditCard,
  shield:ShieldCheck
};


type MenuItemProps = {
  href: string;
  label: string;
  icon: string;
};

export function MenuItem({ href, label, icon }: MenuItemProps) {
  const pathname = usePathname();
  const active = pathname === href;
  const Icon = ICONS[icon];

  const activeStyle = `bg-[${COLORS.lightGreen}] text-white shadow-sm`
  return (
    <Link
      href={href}
      className={clsx(
        "flex items-center gap-3 rounded-md px-3 py-2 text-sm font-medium transition",
        active
          ?  "text-white shadow-sm"
          : "text-gray-700 hover:bg-gray-100"
      )}
    style={{backgroundColor:active?COLORS.primary:''}}>
      <Icon
        size={18}
        className={active ? "text-white" : "text-gray-500"}
      />

      <span>{label}</span>
    </Link>
  );
}
