package com.example.to_docompose.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.data.repository.DataStoreRepository
import com.example.to_docompose.data.repository.TodoRepository
import com.example.to_docompose.ui.util.Action
import com.example.to_docompose.ui.util.CONSTANTS
import com.example.to_docompose.ui.util.ListAppBarState
import com.example.to_docompose.ui.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    val listAppBarState: MutableState<ListAppBarState> = mutableStateOf(ListAppBarState.CLOSED)
    val listAppBarSearchQuery = mutableStateOf("")

    val action = mutableStateOf(Action.NO_ACTION)
    private val _allTask: MutableStateFlow<RequestState<List<ToDoTask>>>
            = MutableStateFlow(RequestState.Idle)
    val allTask: StateFlow<RequestState<List<ToDoTask>>>
        get() = _allTask

    private val _searchedTasks: MutableStateFlow<RequestState<List<ToDoTask>>> = MutableStateFlow(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>>
        get() = _searchedTasks

    fun getAllTask(){
        _allTask.value = RequestState.Loading
        try {
            viewModelScope.launch {
//                delay(1000)
                repository.getAllTask.collect{
                    _allTask.value = RequestState.Success(it)
                }
            }
        }catch (e: Exception){
            _allTask.value = RequestState.Error(e)
        }
    }

    private fun getTaskSortedLow(){
        _allTask.value = RequestState.Loading
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getTaskSortedLowPriority.collect{
                    _allTask.value = RequestState.Success(it)
                }
            }
        }catch (e: Exception){
            _allTask.value = RequestState.Error(e)
        }
    }
    private fun getTaskSortedHigh(){
        _allTask.value = RequestState.Loading
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getTaskSortedHighPriority.collect{
                    _allTask.value = RequestState.Success(it)
                }
            }
        }catch (e: Exception){
            _allTask.value = RequestState.Error(e)
        }
    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?>
    get() = _selectedTask

    fun getTask(taskId: Int){

        viewModelScope.launch {
            repository.getTaskByID(taskId).collect{task ->
                _selectedTask.value = task
            }
        }
    }

    fun searchTask(query: String){
        _searchedTasks.value = RequestState.Loading
        try {
            val searchTaskFlow = repository.searchDatabase("%$query%")
            viewModelScope.launch(Dispatchers.IO) {
                searchTaskFlow.collectLatest { list ->
                    _searchedTasks.value = RequestState.Success(list)
                }
            }
        }catch (e: Exception){
            _searchedTasks.value = RequestState.Error(e)
        }
        listAppBarState.value = ListAppBarState.TRIGGERED
    }

    private val id = mutableStateOf(0)
    val title = mutableStateOf("")
    val description: MutableState<String?> = mutableStateOf("")
    val priority = mutableStateOf(Priority.NONE)

    fun updateFieldsWithCurrentSelectedTask(task: ToDoTask?){

            if (task != null) {
                id.value = task.id
                title.value = task.title
                description.value = task.description
                priority.value = task.priority
            } else {
                id.value = 0
                title.value = ""
                description.value = null
                priority.value = Priority.LOW
            }

    }

    fun updateTitle(newTitle: String){
        if(newTitle.length < CONSTANTS.MAX_TITLE_LENGTH)
            title.value = newTitle
    }
    fun validateFields(): Boolean{
        return title.value.isNotBlank()
    }

    fun handleDatabaseActions(action: Action){
        when (action){
            Action.ADD -> addTask()
            Action.UPDATE -> updateTask()
            Action.DELETE -> deleteTask()
            Action.DELETE_ALL -> deleteAllTask()
            Action.UNDO ->  undoTask()
            Action.NO_ACTION -> {}
        }
        this.action.value = Action.NO_ACTION
    }

    private fun undoTask() {
        addTask()
    }

    private fun deleteAllTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTask()
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(
                ToDoTask(
                    id = id.value,
                    title = title.value,
                    description = description.value,
                    priority = priority.value,
                )
            )
        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(
                ToDoTask(
                    id = id.value,
                    title = title.value,
                    description = description.value,
                    priority = priority.value,
                )
            )
        }
    }

    private fun addTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val task = ToDoTask(title = title.value, description = description.value, priority = priority.value)
            repository.insertTask(task)
        }
        listAppBarState.value = ListAppBarState.CLOSED
    }

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>>
    get() = _sortState

    fun readSortState(){
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch(Dispatchers.IO) {
                dataStoreRepository.getSortOrder().collect {
                    _sortState.value = RequestState.Success(Priority.valueOf(it))
                }
            }
        }catch (e: Exception){
            _sortState.value = RequestState.Error(e)
        }
    }

    fun changeSortPriority(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveSortOrder(priority)
        }
    }

     fun viewByPriority(priority: Priority){
         when(priority){
             Priority.HIGH -> getTaskSortedHigh()
             Priority.MEDIUM -> { }
             Priority.LOW -> getTaskSortedLow()
             Priority.NONE -> getAllTask()
         }
     }
}