package learning.regressors

import data.Data
import data.DataWithResult
import data.WeightedData
import javafx.application.Application.launch
import learning.model.ModelWithTeacher
import org.ejml.LinearSolverSafe
import org.ejml.data.DMatrixRMaj
import org.ejml.dense.row.CommonOps_DDRM
import org.ejml.dense.row.factory.LinearSolverFactory_DDRM
import org.ejml.simple.SimpleMatrix

class LeastSquareRegressor() : ModelWithTeacher {

    private var X: SimpleMatrix? = null

    override fun output(data: Data): Double {
        if (X == null) throw RuntimeException("Regressor is not ready")
        val A = SimpleMatrix(Array(1, {
            data.vector
        }))
        return A.mult(X).elementSum()
    }

    override fun train(data: List<DataWithResult>) {

        val P = DMatrixRMaj(data.size, data.size)
        for (i in 0..data.size - 1) {
            val point = data[i]
            val weight = if (point is WeightedData) point.weight else 1.0
            P.set(i, i, weight)
        }

        val X = DMatrixRMaj(data[0].vector.size, 1)
        val A_ = DMatrixRMaj(data.size, data[0].vector.size)
        val B_ = DMatrixRMaj(data.size, 1)

        for (i in 0..data.size - 1) {
            for (j in 0..data[i].vector.size - 1) {
                A_.set(i, j, data[i].vector[j])
            }
            B_.set(i, 0, data[i].result)
        }

        val A = DMatrixRMaj(data.size, data[0].vector.size)
        val B = DMatrixRMaj(data.size, 1)

        CommonOps_DDRM.mult(P, A_, A)
        CommonOps_DDRM.mult(P, B_, B)

        var solver = LinearSolverFactory_DDRM.leastSquares(X.numRows, X.numCols)
        solver = LinearSolverSafe<DMatrixRMaj>(solver)
        solver.setA(A)
        solver.solve(B, X)

        this.X = SimpleMatrix(X)
    }
}
