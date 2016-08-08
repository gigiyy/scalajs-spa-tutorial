package spatutorial.shared

trait Api {
  // message of the day
  def welcomeMsg(name: String): String

  // get Todo items
  def getAllTodos(): Seq[TodoItem]

  // update a Todo
  def updateTodo(item: TodoItem): Seq[TodoItem]

  // delete a Todo
  def deleteTodo(itemId: String): Seq[TodoItem]

  // list server files
  def listFile(path: String): Seq[FileItem]
}
