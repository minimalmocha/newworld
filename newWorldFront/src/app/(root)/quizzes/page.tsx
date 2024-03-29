import React from "react";
import Quizzes from "./Quizzes";
import Header from "@/app/components/header/page";
import { getQuizzesAPI } from "../../lib/api/quizzes";
import { getUserClearQuizzes } from "@/app/lib/api/mypageapi";
import { getServerSession } from "next-auth";
import { authOptions } from "@/app/utils/authOptions";
import { MySession } from "@/app/types/Session";
import { redirect } from "next/navigation";
const page = async () => {
  const session = (await getServerSession(authOptions)) as MySession;
  if (!session) {
    return redirect("/login");
  }
  const { nickname } = session?.user;
  let data: any = [];
  let page = 0;

  while (true) {
    try {
      const res = await getQuizzesAPI(page);
      data = data.concat(res.content);
      if (res.last) break; // 마지막 페이지에 도달하면 종료
      page++;
    } catch (error) {
      // console.error(error);
      break;
    }
  }

  return (
    <>
      <Header />
      <Quizzes data={data}></Quizzes>
    </>
  );
};

export default page;
